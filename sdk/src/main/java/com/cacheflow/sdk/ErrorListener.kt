package com.cacheflow.sdk

/**
 * ErrorListener is an interface that provides a callback mechanism for handling errors
 * within the CacheFlow SDK. Implement this interface to receive error notifications
 * when exceptions or issues occur during SDK operations.
 */
interface ErrorListener {

    /**
     * Called when an error occurs within the SDK.
     *
     * @param error The exception representing the error that occurred.
     */
    fun onError(error: CacheFlowException)
}