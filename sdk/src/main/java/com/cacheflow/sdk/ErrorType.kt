package com.cacheflow.sdk

/**
 * ErrorType is an enumeration that defines the different types of errors
 * that can occur within the CacheFlow SDK. Each error type is associated
 * with a default error message.
 *
 * @property defaultMessage The default message that describes the error type.
 */
enum class ErrorType(val defaultMessage: String) {

    /** Represents an error related to network issues, such as connectivity problems. */
    NETWORK_ERROR("A network error occurred."),

    /** Represents an error that occurs when parsing the response, typically due to invalid JSON format. */
    PARSE_ERROR("Response is not a valid JSON format."),

    /** Represents an error that occurs during cache operations, such as saving or retrieving data. */
    CACHE_ERROR("An unexpected cache error occurred."),

    /** Represents an error that does not fit into the other predefined categories. */
    UNKNOWN_ERROR("An unknown error occurred.")
}