
# CacheFlow SDK

CacheFlow SDK provides a robust and efficient solution for caching network responses in Android applications. It simplifies network requests by automatically handling caching, and also provides optional file download capabilities.

## Quick Start Guide

### Installation

Add the CacheFlow SDK to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.cacheflow:cacheflow:1.0.0'
}
```

### Setting Up CacheFlow

Before making any requests, initialize CacheFlow with your configuration:

```kotlin
import com.cacheflow.sdk.CacheFlowConfig
import com.cacheflow.sdk.CacheFlow

val cacheFlowConfig = CacheFlowConfig(
    cacheDuration = 60 * 60 * 1000, // 1 hour
    isOfflineModeEnabled = true,
    responseType = MyResponse::class, 
    errorType = MyError::class 
)

CacheFlow.initialize(
    config = cacheFlowConfig,
    context = applicationContext,
    baseUrl = "https://api.example.com"
)
```

### Making Requests with CacheFlow (Flow Usage)

You can make requests using CacheFlow with Flow:

```kotlin
import com.cacheflow.sdk.CacheFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val url = "https://api.example.com/data"

launch {
    CacheFlow.performRequest(url) {
        apiService.getData()
    }.collect { result ->
        when (result) {
            is Result.Success -> {
                val data = result.data
                // Handle success
            }
            is Result.Failure -> {
                val error = result.message
                // Handle error
            }
            is Result.Loading -> {
                // Handle loading state
            }
        }
    }
}
```

### Making Requests with CacheFlow (Request Listener)

Alternatively, you can use a request listener:

```kotlin
import com.cacheflow.sdk.CacheFlow
import com.cacheflow.sdk.ResultListener

CacheFlow.performRequest(
    url = "https://api.example.com/data",
    apiCall = { apiService.getData() },
    listener = object : ResultListener<MyResponse> {
        override fun onLoading() {
            // Handle loading
        }

        override fun onSuccess(data: MyResponse?) {
            // Handle success
        }

        override fun onFailure(message: String) {
            // Handle failure
        }
    }
)
```

### File Downloads

CacheFlow can automatically detect and handle downloadable content:

```kotlin
import com.cacheflow.sdk.CacheFlow
import com.cacheflow.sdk.ResultListener
import java.io.File

CacheFlow.performRequest(
    url = "https://example.com/file.zip",
    apiCall = { apiService.downloadFile() },
    listener = object : ResultListener<File> {
        override fun onLoading() {
            // Handle loading
        }

        override fun onSuccess(data: File?) {
            // Handle file download success
        }

        override fun onFailure(message: String) {
            // Handle failure
        }
    }
)
```

**Note**: If you expect the response to be a downloadable file, ensure you provide a `ResultListener<File>` to handle the download progress and completion.

### Detailed Documentation

For a more in-depth guide, including advanced usage and additional features, refer to the [detailed documentation](README-Detailed-Documentation.md).
