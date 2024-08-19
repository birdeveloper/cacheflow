package com.cacheflow.sdk.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * CacheDatabase is an abstract class that represents the Room database for caching data in the CacheFlow SDK.
 * It defines the database configuration and serves as the main access point for the underlying connection to the app's persisted data.
 */
@Database(entities = [CacheEntity::class], version = 1, exportSchema = false)
abstract class CacheDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO (Data Access Object) for cache-related operations.
     *
     * @return The CacheDao instance to perform database operations.
     */
    abstract fun cacheDao(): CacheDao

    companion object {
        @Volatile
        private var INSTANCE: CacheDatabase? = null

        /**
         * Retrieves the singleton instance of CacheDatabase. If the instance does not exist, it will be created.
         *
         * @param context The application context used to create or retrieve the database.
         * @return The singleton CacheDatabase instance.
         */
        fun getDatabase(context: Context): CacheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CacheDatabase::class.java,
                    "cache_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}