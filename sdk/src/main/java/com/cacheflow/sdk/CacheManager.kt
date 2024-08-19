package com.cacheflow.sdk

import android.util.Log
import com.cacheflow.sdk.CacheFlow.handleException
import com.cacheflow.sdk.cache.CacheDatabase

/**
 * CacheManager is responsible for managing cache operations within the CacheFlow SDK.
 * It handles the clearing of cache and validates the cache's validity based on timestamps.
 *
 * @param cacheDatabase The database instance used for caching data.
 */
class CacheManager(
    private val cacheDatabase: CacheDatabase
) {

    /**
     * Clears the cache either for a specific URL or all cached data if no URL is provided.
     *
     * @param url Optional URL to clear specific cache entries. If null, all cache is cleared.
     * @return A Result indicating success or failure of the cache clearing operation.
     */
    suspend fun clearCache(url: String? = null): Result<Unit> {
        return try {
            if (url != null) {
                Log.d(CacheFlow.TAG, "Clearing cache for URL: $url")
                cacheDatabase.cacheDao().deleteCacheByUrl(url)
            } else {
                Log.d(CacheFlow.TAG, "Clearing all cache")
                cacheDatabase.cacheDao().clearAllCache()
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    /**
     * Checks if the cached data is still valid based on its timestamp and the specified duration.
     *
     * @param timestamp The timestamp when the data was cached.
     * @param duration The maximum duration in milliseconds for which the cache is considered valid.
     * @return True if the cache is valid, false otherwise.
     */
    fun isCacheValid(timestamp: Long, duration: Long): Boolean {
        return System.currentTimeMillis() - timestamp < duration
    }

}