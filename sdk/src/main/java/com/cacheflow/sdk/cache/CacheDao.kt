package com.cacheflow.sdk.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * CacheDao is a Data Access Object (DAO) interface for managing cache operations in the CacheFlow SDK.
 * It provides methods for inserting, querying, and deleting cache entries in the local database.
 */
@Dao
interface CacheDao {

    /**
     * Inserts a cache entry into the database. If a cache entry with the same URL already exists,
     * it will be replaced.
     *
     * @param cacheEntity The cache entity to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cacheEntity: CacheEntity)

    /**
     * Retrieves a cache entry from the database based on the provided URL.
     *
     * @param url The URL of the cached entry to be retrieved.
     * @return The cache entry corresponding to the given URL, or null if not found.
     */
    @Query("SELECT * FROM cache_table WHERE url = :url")
    suspend fun getCacheByUrl(url: String): CacheEntity?

    /**
     * Deletes a specific cache entry from the database based on the provided URL.
     *
     * @param url The URL of the cached entry to be deleted.
     */
    @Query("DELETE FROM cache_table WHERE url = :url")
    suspend fun deleteCacheByUrl(url: String)

    /**
     * Deletes all cache entries from the database.
     */
    @Query("DELETE FROM cache_table")
    suspend fun clearAllCache()
}