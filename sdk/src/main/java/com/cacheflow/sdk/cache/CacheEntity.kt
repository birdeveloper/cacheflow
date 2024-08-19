package com.cacheflow.sdk.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CacheEntity represents a single entry in the cache database within the CacheFlow SDK.
 * Each entry contains the URL as a unique identifier, the response data, and the timestamp when it was cached.
 *
 * @param url The URL that serves as the unique identifier for the cached entry.
 * @param response The JSON response data from the network request, stored as a String.
 * @param timestamp The time in milliseconds when the response was cached.
 */
@Entity(tableName = "cache_table")
data class CacheEntity(
    @PrimaryKey(autoGenerate = false)
    val url: String, // URL acts as the unique identifier for caching

    val response: String, // The JSON response from the network request

    val timestamp: Long // The time when the response was cached
)