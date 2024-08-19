package com.cacheflow.sdk.download

import android.content.Context
import android.os.Environment
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * DownloadManager handles file download operations within the CacheFlow SDK.
 * It uses OkHttp for network operations and provides progress updates and completion notifications via a listener interface.
 *
 * @param context The context used to access system services, such as file storage.
 * @param client The OkHttpClient instance used for making network requests.
 */
class DownloadManager(private val context: Context, private val client: OkHttpClient) {

    /**
     * DownloadListener is an interface for receiving updates on the file download process,
     * including progress, completion, and failure.
     */
    interface DownloadListener {
        /**
         * Called periodically to report the download progress.
         *
         * @param percentage The current download progress as a percentage.
         */
        fun onProgressUpdate(percentage: Int)

        /**
         * Called when the file has been successfully downloaded.
         *
         * @param file The downloaded file.
         */
        fun onDownloadComplete(file: File)

        /**
         * Called when the download fails due to an error.
         *
         * @param exception The exception that caused the download to fail.
         */
        fun onDownloadFailed(exception: Exception)
    }

    /**
     * Initiates a file download from the specified URL and reports the download status
     * via the provided DownloadListener.
     *
     * @param url The URL of the file to be downloaded.
     * @param fileName The name of the file to save on the device.
     * @param listener The listener to receive download status updates.
     */
    fun downloadFile(url: String, fileName: String, listener: DownloadListener) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DownloadManager", "Download failed: ${e.message}")
                listener.onDownloadFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    val errorMessage = "Failed to download file: HTTP ${response.code}"
                    Log.e("DownloadManager", errorMessage)
                    listener.onDownloadFailed(IOException(errorMessage))
                    return
                }

                val inputStream = response.body?.byteStream()
                val totalBytes = response.body?.contentLength() ?: -1
                var downloadedBytes = 0

                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                val outputStream = FileOutputStream(file)

                try {
                    val buffer = ByteArray(8 * 1024) // 8 KB buffer
                    var bytesRead: Int

                    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead
                        val percentage = if (totalBytes > 0) (downloadedBytes * 100 / totalBytes).toInt() else -1
                        listener.onProgressUpdate(percentage)
                    }

                    outputStream.flush()
                    Log.i("DownloadManager", "Download complete: ${file.absolutePath}")
                    listener.onDownloadComplete(file)
                } catch (e: Exception) {
                    Log.e("DownloadManager", "Error during file download: ${e.message}")
                    listener.onDownloadFailed(e)
                } finally {
                    inputStream?.close()
                    outputStream.close()
                }
            }
        })
    }
}