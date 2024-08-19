package com.cacheflow.sdk

/**
 * Result is a sealed class that represents the outcome of an operation within the CacheFlow SDK.
 * It can either be a success, failure, or indicate that the operation is in progress (loading).
 *
 * @param T The type of data returned in the success case.
 */
sealed class Result<out T> {

    /**
     * Represents a successful outcome of an operation, containing the resulting data.
     *
     * @param data The data returned from the successful operation, or null if there is no data.
     */
    data class Success<out T>(val data: T?) : Result<T>()

    /**
     * Represents a failed outcome of an operation, containing an error message and an optional exception.
     *
     * @param message A description of the error that occurred.
     * @param error An optional CacheFlowException providing more details about the error.
     */
    data class Failure(
        val message: String,
        val error: CacheFlowException? = null
    ) : Result<Nothing>()

    /**
     * Represents a loading state, indicating that an operation is in progress.
     */
    object Loading : Result<Nothing>()
}