package com.cacheflow.sdk

import com.cacheflow.sdk.cache.CacheEntity
import com.cacheflow.sdk.config.CacheFlowConfig
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass

// This test class is used to verify the correct functionality of the CacheFlowConfig class.
class CacheFlowConfigTest {

    // Test to ensure that CacheFlowConfig initializes with the default values.
    @Test
    fun testCacheFlowConfigInitializationWithDefaultValues() {
        val config = CacheFlowConfig<CacheEntity, Any>()

        assertEquals("Default cache duration should be 1 hour", 60 * 60 * 1000, config.cacheDuration)
        assertTrue("Offline mode should be enabled by default", config.isOfflineModeEnabled)
        assertNull("ErrorListener should be null by default", config.errorListener)
        assertNull("ResponseType should be null by default", config.responseType)
        assertNull("ErrorType should be null by default", config.errorType)
    }

    // Test to ensure that CacheFlowConfig initializes with custom values.
    @Test
    fun testCacheFlowConfigInitializationWithCustomValues() {
        val customCacheDuration = 2 * 60 * 60 * 1000L // 2 hours
        val customOfflineModeEnabled = false
        val customErrorListener = object : ErrorListener {
            override fun onError(error: CacheFlowException) {
                // Custom error handling logic
            }
        }
        val customResponseType: KClass<CacheEntity> = CacheEntity::class
        val customErrorType: KClass<Any> = Any::class

        val config = CacheFlowConfig(
            cacheDuration = customCacheDuration,
            isOfflineModeEnabled = customOfflineModeEnabled,
            errorListener = customErrorListener,
            responseType = customResponseType,
            errorType = customErrorType
        )

        assertEquals("Custom cache duration should match", customCacheDuration, config.cacheDuration)
        assertFalse("Offline mode should be disabled", config.isOfflineModeEnabled)
        assertNotNull("ErrorListener should not be null", config.errorListener)
        assertEquals("ResponseType should match custom value", customResponseType, config.responseType)
        assertEquals("ErrorType should match custom value", customErrorType, config.errorType)
    }
}