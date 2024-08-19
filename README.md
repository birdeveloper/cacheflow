# CacheFlow SDK

CacheFlow SDK is a comprehensive Android library designed to streamline network requests and efficiently manage response caching. It aims to enhance the performance of your application by minimizing redundant network requests and optimizing response retrieval.

## Features

- **Efficient Cache Management**: Automatically handles caching of network responses.
- **Network Request Handling**: Simplifies the process of making network requests.
- **Error Handling**: Customizable error messages and listeners.
- **File Downloading**: Supports file downloads with progress tracking.

## Getting Started

### Adding CacheFlow to Your Project

To integrate CacheFlow into your Android project, follow these steps:

1. **Add the Dependency**: Include CacheFlow in your project's `build.gradle` file:

    ```groovy
    dependencies {
        implementation 'com.cacheflow.sdk:cacheflow:1.0.0'
    }
    ```

2. **Sync Your Project**: Sync your project with Gradle to fetch and incorporate the CacheFlow library.

### Setting Up CacheFlow

1. **Initialize CacheFlow**: Configure CacheFlow in your `Application` class or main activity. Set up cache duration, offline mode, and error listeners as needed.

    ```kotlin
    CacheFlow.initialize(
        CacheFlowConfig(
            cacheDuration = 60 * 60 * 1000, // Cache duration of 1 hour
            isOfflineModeEnabled = true,
            errorListener = object : ErrorListener {
                override fun onError(error: CacheFlowException) {
                    // Handle any errors that occur
                }
            }
        )
    )
    ```

2. **Configure Network Client**: Set up a `NetworkClient` instance to handle your network operations. Provide the base URL and optionally customize the `OkHttpClient`.

    ```kotlin
    val networkClient = NetworkClient.getInstance(
        baseUrl = "https://api.example.com",
        okHttpClient = OkHttpClient.Builder().build()
    )
    ```

### Making Requests with CacheFlow

1. **Define API Endpoints**: Create a Retrofit service interface to define your API endpoints.

    ```kotlin
    interface ApiService {
        @GET("endpoint")
        suspend fun getData(): Response<DataModel>
    }
    ```

2. **Perform Network Requests**: Use CacheFlow to handle network requests and cache responses. You can use either Flow for reactive updates or callback-based listeners for real-time notifications.

    ```kotlin
    fun fetchData() {
        val service = networkClient.createService(ApiService::class.java)
        // Make network requests and handle responses
    }
    ```

## Additional Documentation

For a more detailed explanation of CacheFlow's features, configuration options, and advanced usage, please refer to the [comprehensive documentation](link-to-detailed-markdown-file).

## License

This project is licensed under the [MIT License](LICENSE).
