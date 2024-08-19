
# CacheFlow SDK - Comprehensive Documentation

CacheFlow SDK is a robust solution for caching network responses in Android applications. This guide provides a detailed explanation of the SDK's features, setup, usage, testing, and more.

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

#### 1. Configure CacheFlow

Initialize CacheFlow by creating an instance of the `CacheFlow` class and providing necessary configuration options.

```kotlin
val cacheFlow = CacheFlow.Builder(context)
    .setCacheDirectory(File(context.cacheDir, "cache"))
    .setCacheSize(10 * 1024 * 1024) // 10 MB
    .build()
```

#### 2. Set Up Network Client

Integrate CacheFlow with your network client to cache network responses. Example using Retrofit:

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/")
    .client(CacheFlow.getOkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

## Making Requests with CacheFlow

Once CacheFlow is set up, you can make network requests and utilize caching seamlessly.

#### 1. Making a Request

Example of making a network request using Retrofit and caching responses:

```kotlin
interface ApiService {
    @GET("data")
    suspend fun getData(): Response<Data>
}
```

#### 2. Handling Responses

Retrieve data from the cache or make a network request if not available in the cache.

```kotlin
val response = apiService.getData()
if (response.isSuccessful) {
    val data = response.body()
    // Process the data
}
```

## Advanced Usage

### Handling Different Request Types

CacheFlow supports various types of requests such as GET and POST. Configure and handle these requests based on your needs.

```kotlin
@POST("submit")
suspend fun submitData(@Body data: RequestData): Response<SubmitResponse>
```

### Downloading Files

To handle file downloads, use the `CacheFlow` download manager.

```kotlin
val downloadRequest = DownloadRequest.Builder("https://example.com/file.zip")
    .setDestination(File(context.getExternalFilesDir(null), "file.zip"))
    .build()
CacheFlow.getDownloadManager().enqueue(downloadRequest)
```

## Data Storage

CacheFlow stores cached data in a local SQLite database using Room. Data is stored in a `CacheEntity` table, which includes the URL, the cached response, and a timestamp.

### 1. Cache Entity

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

### 2. Cache DAO

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

### Supported Request Types

- **GET Requests**: The primary type of request handled by CacheFlow, suitable for retrieving data.

### Handling Responses

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

Testing is crucial to ensure the reliability of CacheFlow SDK. Here's an overview of how to test different components:

### Unit Tests

Write unit tests for individual components such as `CacheManager`, `CacheDao`, and network operations. Use mocking frameworks to simulate interactions and verify behavior.

```kotlin
class CacheManagerTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cacheDao: CacheDao
    private lateinit var cacheManager: CacheManager

    @Before
    fun setup() {
        cacheDao = mock(CacheDao::class.java)
        cacheManager = CacheManager(cacheDao)
    }

    @Test
    fun testInsertCache() = runBlocking {
        val cacheEntity = CacheEntity("url", "data")
        `when`(cacheDao.insertCache(cacheEntity)).thenReturn(1)

        cacheManager.insertCache(cacheEntity)

        verify(cacheDao).insertCache(cacheEntity)
    }

    @Test
    fun testGetCacheByUrl() = runBlocking {
        val url = "url"
        val cacheEntity = CacheEntity(url, "data")
        `when`(cacheDao.getCacheByUrl(url)).thenReturn(cacheEntity)

        val result = cacheManager.getCacheByUrl(url)

        assertEquals(cacheEntity, result)
    }
}
```

### Integration Tests

Test the integration of CacheFlow with your network client and database to ensure seamless operation across different components.

### Edge Case Tests

Handle edge cases such as network failures, cache expiration, and large file downloads to ensure robustness.

## Additional Resources

For more details on CacheFlow SDK, refer to the [comprehensive documentation](URL_TO_DETAILED_DOCUMENTATION).
