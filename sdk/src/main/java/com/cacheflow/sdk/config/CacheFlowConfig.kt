package com.cacheflow.sdk.config

import com.cacheflow.sdk.ErrorListener
import kotlin.reflect.KClass

/**
 * CacheFlowConfig is a configuration class for setting up various options and behaviors
 * within the CacheFlow SDK. It allows customization of cache duration, offline mode,
 * error handling, and response/error model types.
 *
 * @param T The type of the expected response model.
 * @param E The type of the expected error model.
 * @param cacheDuration The duration for which the cache is valid, in milliseconds. The default is 1 hour.
 * @param isOfflineModeEnabled A flag to determine whether offline mode is enabled, allowing cached responses when offline.
 * @param errorListener An optional listener for handling errors that occur during SDK operations.
 * @param responseType The user-provided class type for parsing network responses. If null, a generic response is used.
 * @param errorType The user-provided class type for parsing error responses. If null, a generic error handling is used.
 */
data class CacheFlowConfig<T: Any, E: Any>(
    val cacheDuration: Long = 60 * 60 * 1000, // Cache duration in milliseconds, default is 1 hour
    val isOfflineModeEnabled: Boolean = true, // Determines if offline mode is enabled
    val errorListener: ErrorListener? = null, // Listener for handling errors
    val responseType: KClass<T>? = null, // User-provided response model class
    val errorType: KClass<E>? = null // User-provided error model class
)