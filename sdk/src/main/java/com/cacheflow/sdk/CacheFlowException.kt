package com.cacheflow.sdk

/**
 * Represents a custom exception hierarchy used within the CacheFlow SDK.
 * This sealed class provides different types of exceptions that can be
 * encountered during network operations, parsing, caching, or unknown errors.
 *
 * @param errorType The type of error associated with the exception.
 * @param customMessage An optional custom error message that overrides the default message.
 */
sealed class CacheFlowException(
    errorType: ErrorType,
    customMessage: String? = null
) : Exception(customMessage ?: errorType.defaultMessage) {

    /**
     * Represents a network-related error within the SDK.
     *
     * @param customMessage An optional custom error message.
     */
    class NetworkError(customMessage: String? = null) :
        CacheFlowException(ErrorType.NETWORK_ERROR, customMessage)

    /**
     * Represents a parsing-related error within the SDK, typically occurring
     * during the conversion of JSON or other data formats.
     *
     * @param customMessage An optional custom error message.
     */
    class ParseError(customMessage: String? = null) :
        CacheFlowException(ErrorType.PARSE_ERROR, customMessage)

    /**
     * Represents an error related to caching operations within the SDK,
     * such as issues with saving or retrieving data from the cache.
     *
     * @param customMessage An optional custom error message.
     */
    class CacheError(customMessage: String? = null) :
        CacheFlowException(ErrorType.CACHE_ERROR, customMessage)

    /**
     * Represents an unknown error that does not fit into the other categories.
     *
     * @param customMessage An optional custom error message.
     */
    class UnknownError(customMessage: String? = null) :
        CacheFlowException(ErrorType.UNKNOWN_ERROR, customMessage)
}