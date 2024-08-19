
# CacheFlow SDK - Detailed Documentation

CacheFlow SDK provides a robust solution for caching network responses in Android applications. This detailed guide covers configuration, supported request types, data storage, file download, and usage with code examples.

## Features

- **Cache Management**: Efficiently manage cached responses with automatic expiration and manual invalidation support.
- **Network Request Handling**: Simplified network requests with built-in caching capabilities.
- **File Download**: Handle file downloads with progress updates and completion callbacks.

## Getting Started

### Installation

To integrate CacheFlow SDK into your Android project, add the following dependency to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.cacheflow:cacheflow:1.0.0'
}
```

### Setting Up CacheFlow

#### 1. Configure CacheFlow:

To configure CacheFlow, you need to initialize it with `CacheFlowConfig`. Here’s an example of how to do this:

```kotlin
import com.cacheflow.sdk.CacheFlowConfig
import com.cacheflow.sdk.cache.CacheDatabase
import com.cacheflow.sdk.network.NetworkClient

// Create CacheFlowConfig instance
val cacheFlowConfig = CacheFlowConfig(
    cacheDuration = 60 * 60 * 1000, // 1 hour
    isOfflineModeEnabled = true,
    responseType = MyResponse::class, // Specify the response model class
    errorType = MyError::class // Specify the error model class
)

// Initialize CacheFlow
CacheFlow.initialize(context, cacheFlowConfig)
```

#### 2. Configure Network Client:

Set up the network client for handling network requests:

```kotlin
import com.cacheflow.sdk.network.NetworkClient
import okhttp3.OkHttpClient

// Create OkHttpClient instance
val okHttpClient = OkHttpClient.Builder()
    .build()

// Initialize NetworkClient
val networkClient = NetworkClient.getInstance(
    baseUrl = "https://api.example.com",
    okHttpClient = okHttpClient
)
```

## Making Requests with CacheFlow

### 1. Making Network Requests:

Use the `NetworkClient` to create and make network requests. Here’s how you can use it:

```kotlin
interface ApiService {
    @GET("data")
    suspend fun getData(): Response<MyResponse>
}

// Create ApiService instance
val apiService = networkClient.createService(ApiService::class.java)

// Make network request
val response = apiService.getData()
if (response.isSuccessful) {
    val data = response.body()
    // Handle the data
} else {
    // Handle the error
}
```

### 2. Handling Responses with CacheFlow:

To handle cached responses, use the `CacheFlow` API. The recommended way to retrieve data is using Kotlin’s Flow API:

#### Example: Fetching Data with Flow

```kotlin
import com.cacheflow.sdk.CacheFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val url = "https://api.example.com/data"

// Example using Flow to collect data
launch {
    CacheFlow.performRequest(url, apiService::getData).collect { result ->
        when (result) {
            is Result.Loading -> {
                // Handle the loading state
            }
            is Result.Success -> {
                val data = result.data
                // Handle the successful response data
            }
            is Result.Failure -> {
                val message = result.message
                // Handle the error message
            }
        }
    }
}
```

#### Example: Using Flow for File Downloads

```kotlin
import com.cacheflow.sdk.download.DownloadManager
import kotlinx.coroutines.flow.collect

val fileUrl = "https://example.com/file.zip"
val fileName = "file.zip"

launch {
    CacheFlow.performRequest(fileUrl, apiService::downloadFile).collect { result ->
        when (result) {
            is Result.Loading -> {
                // Show download progress
            }
            is Result.Success -> {
                val file = result.data
                // Handle the successful download
            }
            is Result.Failure -> {
                val message = result.message
                // Handle download failure
            }
        }
    }
}
```

### 3. Using Request Listener with CacheFlow

Although Flow is recommended, you can also use a `ResultListener` to handle the responses:

```kotlin
CacheFlow.performRequest(url, apiService::getData, object : ResultListener<MyResponse> {
    override fun onLoading() {
        // Handle loading state
    }

    override fun onSuccess(data: MyResponse?) {
        // Handle success
    }

    override fun onFailure(message: String) {
        // Handle error
    }
})
```

## Data Storage

CacheFlow stores cached data in a local SQLite database using Room. Data is stored in a `CacheEntity` table, which includes the URL, the cached response, and a timestamp.

### 1. Cache Entity:

```kotlin
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_table")
data class CacheEntity(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val response: String,
    val timestamp: Long
)
```

### 2. Cache DAO:

```kotlin
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cacheEntity: CacheEntity): Int

    @Query("SELECT * FROM cache_table WHERE url = :url")
    suspend fun getCacheByUrl(url: String): CacheEntity?

    @Query("DELETE FROM cache_table WHERE url = :url")
    suspend fun deleteCacheByUrl(url: String): Int

    @Query("DELETE FROM cache_table")
    suspend fun clearAllCache(): Int
}
```

## Request Types and Data Handling

CacheFlow supports various types of requests and handles them efficiently. You can make GET requests and handle responses with caching. For more complex scenarios, CacheFlow also supports handling errors and integrating with different data types.

### 1. Supported Request Types:

- **GET Requests**: The primary type of request handled by CacheFlow, suitable for retrieving data.
- **File Downloads**: CacheFlow supports downloading files of types such as images, videos, PDFs, and more.

### 2. Handling Responses:

Responses can be handled through the `Result` class which provides methods to deal with success, failure, and loading states.

```kotlin
import com.cacheflow.sdk.Result

fun handleResponse(result: Result<MyResponse>) {
    when (result) {
        is Result.Success -> {
            val data = result.data
            // Handle the successful response data
        }
        is Result.Failure -> {
            val message = result.message
            // Handle the error message
        }
        is Result.Loading -> {
            // Handle the loading state
        }
    }
}
```

## Testing

### 1. Writing Unit Tests:

Unit tests ensure that each component of the SDK functions correctly. Use Mockito for mocking dependencies and testing various scenarios.

```kotlin
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CacheManagerTest {

    @Test
    fun testInsertCache() {
        val mockCacheDao = mock(CacheDao::class.java)
        val cacheManager = CacheManager(mockCacheDao)

        val cacheEntity = CacheEntity("https://api.example.com/data", "response", System.currentTimeMillis())
        cacheManager.insertCache(cacheEntity)

        verify(mockCacheDao).insertCache(cacheEntity)
    }
}
```

### 2. Integration Testing:

Integration tests ensure that different parts of the SDK work together as expected. These tests should include scenarios where multiple components interact.

### 3. Edge Case Testing:

Test for edge cases such as network failures, empty responses, and invalid data.

## Conclusion

CacheFlow SDK offers an efficient and flexible solution for caching network requests in Android applications. With its easy-to-use API, powerful caching mechanisms, and support for file downloads, it provides developers with a comprehensive toolset to enhance the performance and reliability of their apps.
