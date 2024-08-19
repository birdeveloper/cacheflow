package com.cacheflow.sdk

import android.content.Context
import android.util.Log
import com.cacheflow.sdk.cache.CacheDatabase
import com.cacheflow.sdk.cache.CacheEntity
import com.cacheflow.sdk.config.CacheFlowConfig
import com.cacheflow.sdk.download.DownloadManager
import com.cacheflow.sdk.network.NetworkClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import kotlin.reflect.KClass

object CacheFlow {

    internal val TAG = CacheFlow::class.java.simpleName

    // Configuration object containing settings and types for the SDK
    private lateinit var config: CacheFlowConfig<*, *>

    // Instances of the necessary components for caching, networking, and downloading
    private lateinit var cacheDatabase: CacheDatabase
    private lateinit var networkClient: NetworkClient
    private lateinit var downloadManager: DownloadManager

    /**
     * Initializes the CacheFlow SDK with the necessary configurations.
     *
     * @param config Configuration object containing the response and error types.
     * @param context Android context for accessing application resources.
     * @param baseUrl The base URL for network requests.
     * @param okHttpClient Optional custom OkHttpClient instance.
     * @param retrofitBuilder Optional custom Retrofit.Builder instance.
     */
    fun <T : Any, E : Any> initialize(
        config: CacheFlowConfig<T, E>,
        context: Context,
        baseUrl: String,
        okHttpClient: okhttp3.OkHttpClient? = null,
        retrofitBuilder: Retrofit.Builder? = null
    ) {
        this.config = config
        cacheDatabase = CacheDatabase.getDatabase(context)
        networkClient = NetworkClient.getInstance(baseUrl, okHttpClient, retrofitBuilder)
        downloadManager = DownloadManager(context, okHttpClient ?: okhttp3.OkHttpClient())
    }

    /**
     * Performs a network request with optional caching and handles file downloads automatically.
     *
     * @param url The URL for the request.
     * @param apiCall The suspend function that executes the network request.
     * @param listener Optional listener for handling result callbacks.
     * @return A Flow emitting the Result of the request.
     */
    suspend fun <T : Any> performRequest(
        url: String,
        apiCall: suspend () -> Response<T>,
        listener: ResultListener<T>? = null
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)
        listener?.onLoading()

        try {
            Log.d(TAG, "Performing request for URL: $url")
            val response = apiCall.invoke()
            val contentType = response.headers()["Content-Type"]

            if (contentType != null && isDownloadableContent(contentType)) {
                Log.d(TAG, "Downloadable content detected. Starting download...")

                // Check if the response body is a file, and handle the download process
                if (File::class.isInstance(response.body())) {
                    val fileName = getFileNameFromUrl(url)
                    FileDownloadService(downloadManager).downloadFile(url, fileName)
                        .collect { downloadResult ->
                            when (downloadResult) {
                                is Result.Success -> {
                                    try {
                                        @Suppress("UNCHECKED_CAST")
                                        emit(Result.Success(downloadResult.data as T))
                                        listener?.onSuccess(downloadResult.data as T)
                                    } catch (e: Exception) {
                                        val errorMessage =
                                            "Error casting File to expected type: ${e.message}"
                                        Log.e(TAG, errorMessage, e)
                                        emit(Result.Failure(errorMessage))
                                        listener?.onFailure(errorMessage)
                                    }
                                }

                                is Result.Failure -> {
                                    emit(Result.Failure(downloadResult.message))
                                    listener?.onFailure(downloadResult.message)
                                }

                                is Result.Loading -> {
                                    emit(Result.Loading)
                                    listener?.onLoading()
                                }
                            }
                        }
                } else {
                    val errorMessage =
                        "Error: Expected response type File but found ${response.body()?.javaClass?.name}"
                    Log.e(TAG, errorMessage)
                    emit(Result.Failure(errorMessage))
                    listener?.onFailure(errorMessage)
                }
            } else {
                // Handle normal response with optional caching
                val cachedResponse = cacheDatabase.cacheDao().getCacheByUrl(url)
                if (cachedResponse != null && config.isOfflineModeEnabled && CacheManager(
                        cacheDatabase
                    ).isCacheValid(cachedResponse.timestamp, config.cacheDuration)
                ) {
                    Log.d(TAG, "Returning cached response for URL: $url")
                    val parsedResponse: T =
                        parseResponse(cachedResponse.response, config.responseType) as T
                    emit(Result.Success(parsedResponse))
                    listener?.onSuccess(parsedResponse)
                } else {
                    Log.d(TAG, "Saving new response to cache for URL: $url")
                    val responseString = Gson().toJson(response.body())
                    cacheDatabase.cacheDao().insertCache(
                        CacheEntity(url, responseString, System.currentTimeMillis())
                    )
                    emit(Result.Success(response.body()))
                    listener?.onSuccess(response.body())
                }
            }
        } catch (e: Exception) {
            val failureResult = handleException(e)
            emit(failureResult)
            listener?.onFailure(failureResult.message)
        }
    }

    /**
     * Handles exceptions that occur during network requests or caching.
     *
     * @param e The exception that occurred.
     * @return A Result.Failure containing the error information.
     */
    internal fun handleException(e: Exception): Result.Failure {
        val errorType = when (e) {
            is HttpException -> ErrorType.NETWORK_ERROR
            is JsonSyntaxException -> ErrorType.PARSE_ERROR
            else -> ErrorType.UNKNOWN_ERROR
        }

        val exception = when (errorType) {
            ErrorType.NETWORK_ERROR -> CacheFlowException.NetworkError(e.message)
            ErrorType.PARSE_ERROR -> CacheFlowException.ParseError(e.message)
            ErrorType.CACHE_ERROR -> CacheFlowException.CacheError(e.message)
            ErrorType.UNKNOWN_ERROR -> CacheFlowException.UnknownError(e.message)
        }

        Log.e(TAG, "Error occurred: ${e.message}", e) // Log the error for debugging
        return Result.Failure(e.message ?: "An error occurred", exception)
    }

    /**
     * Parses a response string into a user-provided model or returns a JSONObject.
     *
     * @param response The raw response string.
     * @param responseType The type to which the response should be parsed.
     * @return The parsed response of type T or a JSONObject.
     */
    private fun <T : Any> parseResponse(response: String, responseType: KClass<T>?): T {
        return if (responseType != null) {
            Gson().fromJson(response, responseType.java)
        } else {
            JSONObject(response) as T
        }
    }

    /**
     * Handles API error responses, potentially converting them into a user-provided error model.
     *
     * @param responseBody The raw error response body.
     * @param errorType The type to which the error should be parsed.
     * @param errorMessage A fallback error message if parsing fails.
     * @return A CacheFlowException containing the error information.
     */
    private fun <E : Any> handleApiError(
        responseBody: ResponseBody?,
        errorType: KClass<E>?,
        errorMessage: String
    ): CacheFlowException {
        return try {
            val errorJson = responseBody?.string()
            val errorResponse = if (errorType != null) {
                Gson().fromJson(errorJson, errorType.java)
            } else {
                null
            }

            CacheFlowException.NetworkError(
                customMessage = errorResponse?.toString() ?: "Something went wrong: $errorMessage"
            )
        } catch (e: JsonSyntaxException) {
            CacheFlowException.ParseError("Something went wrong: $errorMessage")
        }
    }

    /**
     * Checks if the content type indicates a downloadable file.
     *
     * @param contentType The content type to check.
     * @return True if the content type is associated with downloadable files, false otherwise.
     */
    private fun isDownloadableContent(contentType: String): Boolean {
        return contentType.startsWith(Constants.CONTENT_TYPE_VIDEO) ||         // Video files
                contentType.startsWith(Constants.CONTENT_TYPE_AUDIO) ||         // Audio files
                contentType.startsWith(Constants.CONTENT_TYPE_PDF) ||           // PDF files
                contentType.startsWith(Constants.CONTENT_TYPE_ZIP) ||           // ZIP archive files
                contentType.startsWith(Constants.CONTENT_TYPE_MSWORD) ||        // Microsoft Word files
                contentType.startsWith(Constants.CONTENT_TYPE_EXCEL) ||         // Microsoft Excel files
                contentType.startsWith(Constants.CONTENT_TYPE_POWERPOINT) ||    // Microsoft PowerPoint files
                contentType.startsWith(Constants.CONTENT_TYPE_OPENXML) ||       // Office Open XML files
                contentType.startsWith(Constants.CONTENT_TYPE_IMAGE) ||         // Image files
                contentType == Constants.CONTENT_TYPE_OCTET_STREAM              // Generic binary files (unknown file types)
    }

    /**
     * Extracts the file name from a URL.
     *
     * @param url The URL from which to extract the file name.
     * @return The extracted file name.
     */
    private fun getFileNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1)
    }
}