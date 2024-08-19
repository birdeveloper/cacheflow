/*
package com.cacheflow.sdk

import com.cacheflow.sdk.network.NetworkClient
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET

/**
 * Unit tests for the NetworkClient class, which is responsible for creating and managing
 * Retrofit instances and making network requests.
 *
 * This test class uses MockWebServer to simulate HTTP responses and validate the
 * behavior of NetworkClient in different scenarios.
 */
class NetworkClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var networkClient: NetworkClient

    /**
     * A sample Retrofit service interface for testing purposes.
     */
    interface TestService {
        @GET("/test")
        fun getTestResponse(): Call<String>
    }

    /**
     * Setup method to initialize MockWebServer and NetworkClient before each test.
     * MockWebServer simulates a web server that allows us to test how NetworkClient
     * handles HTTP responses.
     */
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        networkClient = NetworkClient.getInstance(
            baseUrl = mockWebServer.url("/").toString(),
            okHttpClient = OkHttpClient.Builder().build()
        )
    }

    /**
     * Cleanup method to shut down MockWebServer after each test.
     * This ensures that the server is properly closed and resources are released.
     */
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Test to verify that the NetworkClient creates a valid Retrofit service.
     * This test ensures that a Retrofit service interface can be created and
     * that it is not null.
     */
    @Test
    fun testCreateService() {
        val service = networkClient.createService(TestService::class.java)
        assertNotNull("Retrofit service should not be null", service)
    }

    /**
     * Test to verify that the NetworkClient can successfully make a network request
     * and parse the response. This test uses MockWebServer to simulate a successful
     * HTTP response and verifies that the NetworkClient handles it correctly.
     */
    @Test
    fun testSuccessfulRequest() {
        // Simulate a successful HTTP response with a body of "Success"
        mockWebServer.enqueue(MockResponse().setBody("Success").setResponseCode(200))

        val service = networkClient.createService(TestService::class.java)
        val response = service.getTestResponse().execute()

        assertTrue("Request should be successful", response.isSuccessful)
        assertEquals("Response body should match", "Success", response.body())
    }

    /**
     * Test to verify that the NetworkClient handles HTTP errors correctly.
     * This test simulates an HTTP 404 error and checks that the error is handled as expected.
     */
    @Test
    fun testErrorResponse() {
        // Simulate an HTTP 404 response
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val service = networkClient.createService(TestService::class.java)
        val response = service.getTestResponse().execute()

        assertFalse("Request should not be successful", response.isSuccessful)
        assertEquals("Response code should be 404", 404, response.code())
    }

    /**
     * Test to verify that NetworkClient is a singleton.
     * This test ensures that only one instance of NetworkClient is created and reused.
     */
    @Test
    fun testSingletonBehavior() {
        val firstInstance = NetworkClient.getInstance(
            baseUrl = mockWebServer.url("/").toString(),
            okHttpClient = OkHttpClient.Builder().build()
        )

        val secondInstance = NetworkClient.getInstance(
            baseUrl = mockWebServer.url("/").toString(),
            okHttpClient = OkHttpClient.Builder().build()
        )

        assertSame("NetworkClient should be a singleton", firstInstance, secondInstance)
    }
}
 */