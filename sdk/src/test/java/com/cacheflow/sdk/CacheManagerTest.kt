/*
package com.cacheflow.sdk

import com.cacheflow.sdk.cache.CacheDao
import com.cacheflow.sdk.cache.CacheDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CacheManagerTest {

    private lateinit var cacheManager: CacheManager
    private lateinit var cacheDatabase: CacheDatabase
    private lateinit var cacheDao: CacheDao

    @Before
    fun setUp() {
        cacheDao = mock(CacheDao::class.java)
        cacheDatabase = mock(CacheDatabase::class.java)
        `when`(cacheDatabase.cacheDao()).thenReturn(cacheDao)
        cacheManager = CacheManager(cacheDatabase)
    }

    @Test
    fun testIsCacheValid() {
        val currentTime = System.currentTimeMillis()
        assertTrue(cacheManager.isCacheValid(currentTime - 1000, 2000))
        assertFalse(cacheManager.isCacheValid(currentTime - 3000, 2000))
    }

    @Test
    fun testClearCacheForUrl() = runBlocking {
        val url = "http://example.com"
        `when`(cacheDao.deleteCacheByUrl(url)).thenReturn(Unit)

        val result = cacheManager.clearCache(url)

        assertTrue(result is Result.Success)
        verify(cacheDao).deleteCacheByUrl(url)
    }

    @Test
    fun testClearAllCache() = runBlocking {
        `when`(cacheDao.clearAllCache()).thenReturn(Unit)

        val result = cacheManager.clearCache(null)

        assertTrue(result is Result.Success)
        verify(cacheDao).clearAllCache()
    }
}
 */