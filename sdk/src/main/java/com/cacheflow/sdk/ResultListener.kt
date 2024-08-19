package com.cacheflow.sdk

import java.io.File

/**
 * ResultListener is an interface that provides callback methods for handling various
 * states of an operation within the CacheFlow SDK. It can be used to receive updates
 * on loading, success, failure, and file download progress.
 *
 * @param T The type of data expected in the success case.
 */
interface ResultListener<T> {

    /**
     * Called when an operation is in progress, indicating a loading state.
     */
    fun onLoading()

    /**
     * Called when an operation completes successfully.
     *
     * @param data The data returned from the operation, or null if no data is returned.
     */
    fun onSuccess(data: T?)

    /**
     * Called when an operation fails.
     *
     * @param message A description of the error that occurred.
     */
    fun onFailure(message: String)

    /**
     * Called to provide updates on the progress of a file download operation.
     *
     * @param percentage The current download progress as a percentage.
     */
    fun onFileDownloadProgress(percentage: Int) {}

    /**
     * Called when a file download operation completes successfully.
     *
     * @param file The downloaded file.
     */
    fun onFileDownloadSuccess(file: File) {}
}