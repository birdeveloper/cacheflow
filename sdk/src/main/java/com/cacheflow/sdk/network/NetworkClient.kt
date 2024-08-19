package com.cacheflow.sdk.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * NetworkClient is a singleton class that manages the creation of Retrofit instances
 * for network communication within the CacheFlow SDK. It supports customization of
 * OkHttpClient and Retrofit.Builder during initialization.
 *
 * @param baseUrl The base URL for the network requests.
 * @param okHttpClient An optional OkHttpClient instance for handling HTTP requests.
 * @param retrofitBuilder An optional Retrofit.Builder instance for further customization.
 */
class NetworkClient private constructor(
    private val baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    retrofitBuilder: Retrofit.Builder? = null
) {

    private val okHttpClient: OkHttpClient = okHttpClient ?: OkHttpClient.Builder().build()

    private val retrofit: Retrofit = (retrofitBuilder ?: Retrofit.Builder())
        .baseUrl(baseUrl)
        .client(this.okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    /**
     * Creates and returns a Retrofit service interface implementation.
     *
     * @param T The service interface class.
     * @param serviceClass The class of the service interface.
     * @return The implementation of the service interface.
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    companion object {
        @Volatile
        private var INSTANCE: NetworkClient? = null

        /**
         * Retrieves the singleton instance of NetworkClient. If the instance does not exist, it will be created.
         *
         * @param baseUrl The base URL for the network requests.
         * @param okHttpClient An optional OkHttpClient instance for handling HTTP requests.
         * @param retrofitBuilder An optional Retrofit.Builder instance for further customization.
         * @return The singleton NetworkClient instance.
         */
        fun getInstance(
            baseUrl: String,
            okHttpClient: OkHttpClient? = null,
            retrofitBuilder: Retrofit.Builder? = null
        ): NetworkClient {
            return INSTANCE ?: synchronized(this) {
                val instance = NetworkClient(baseUrl, okHttpClient, retrofitBuilder)
                INSTANCE = instance
                instance
            }
        }
    }
}