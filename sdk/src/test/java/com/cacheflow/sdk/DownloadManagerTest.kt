/*
package com.cacheflow.sdk

import android.content.Context
import com.cacheflow.sdk.download.DownloadManager
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.io.File

class DownloadManagerTest {

    private lateinit var downloadManager: DownloadManager
    private lateinit var mockContext: Context
    private lateinit var mockOkHttpClient: OkHttpClient
    private lateinit var mockListener: DownloadManager.DownloadListener

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        mockOkHttpClient = mock(OkHttpClient::class.java)
        mockListener = mock(DownloadManager.DownloadListener::class.java)
        downloadManager = DownloadManager(mockContext, mockOkHttpClient)
    }

    @Test
    fun testDownloadFileSuccess() {
        val url = "http://example.com/file.zip"
        val fileName = "file.zip"
        val mockFile = mock(File::class.java)
        val mockResponse = mock(Response::class.java)

        `when`(mockResponse.isSuccessful).thenReturn(true)

        // Simulate the download process
        downloadManager.downloadFile(url, fileName, mockListener)

        // Verify that the listener methods are called appropriately
        verify(mockListener, times(1)).onDownloadComplete(mockFile)
    }

    @Test
    fun testDownloadFileFailure() {
        val url = "http://example.com/file.zip"
        val fileName = "file.zip"
        val mockResponse = mock(Response::class.java)

        `when`(mockResponse.isSuccessful).thenReturn(false)

        // Simulate the download process
        downloadManager.downloadFile(url, fileName, mockListener)

        // Verify that the listener methods are called appropriately
        verify(mockListener, times(1)).onDownloadFailed(any())
    }
}
 */