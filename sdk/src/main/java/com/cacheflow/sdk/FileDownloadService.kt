package com.cacheflow.sdk

import com.cacheflow.sdk.download.DownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

/**
 * FileDownloadService is responsible for managing file download operations within the CacheFlow SDK.
 * It utilizes the DownloadManager to perform downloads and emits the download status through a Flow.
 *
 * @param downloadManager The DownloadManager instance used to manage download operations.
 */
class FileDownloadService(
    private val downloadManager: DownloadManager
) {

    /**
     * Initiates a file download from the given URL and emits the download progress, success, or failure.
     *
     * @param url The URL from which the file should be downloaded.
     * @param fileName The name of the file to be saved on the device.
     * @param listener An optional listener to receive real-time updates on the download status.
     * @return A Flow emitting the Result of the download operation, which can be Loading, Success, or Failure.
     */
    fun downloadFile(
        url: String,
        fileName: String,
        listener: ResultListener<File>? = null
    ): Flow<Result<File>> = flow {
        val scope = CoroutineScope(Dispatchers.IO)
        downloadManager.downloadFile(url, fileName, object : DownloadManager.DownloadListener {

            override fun onProgressUpdate(percentage: Int) {
                scope.launch {
                    emit(Result.Loading) // Emit loading state when the download starts
                    listener?.onLoading()
                }
            }

            override fun onDownloadComplete(file: File) {
                scope.launch {
                    emit(Result.Success(file)) // Emit success state when the download completes
                    listener?.onSuccess(file)
                }
            }

            override fun onDownloadFailed(exception: Exception) {
                scope.launch {
                    val failureMessage = exception.message ?: "Download failed"
                    emit(Result.Failure(failureMessage)) // Emit failure state if the download fails
                    listener?.onFailure(failureMessage)
                }
            }
        })
    }
}