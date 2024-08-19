package com.cacheflow.sdk

/**
 * Constants object holds various constant values used across the CacheFlow SDK.
 * This includes content type headers for identifying file types and common messages.
 */
object Constants {
    // Content-Type headers

    /** Represents video content type (e.g., "video/mp4"). */
    const val CONTENT_TYPE_VIDEO = "video/"

    /** Represents audio content type (e.g., "audio/mpeg"). */
    const val CONTENT_TYPE_AUDIO = "audio/"

    /** Represents PDF document content type. */
    const val CONTENT_TYPE_PDF = "application/pdf"

    /** Represents ZIP archive content type. */
    const val CONTENT_TYPE_ZIP = "application/zip"

    /** Represents Microsoft Word document content type. */
    const val CONTENT_TYPE_MSWORD = "application/msword"

    /** Represents Microsoft Excel spreadsheet content type. */
    const val CONTENT_TYPE_EXCEL = "application/vnd.ms-excel"

    /** Represents Microsoft PowerPoint presentation content type. */
    const val CONTENT_TYPE_POWERPOINT = "application/vnd.ms-powerpoint"

    /** Represents Office Open XML document content type (e.g., DOCX, XLSX). */
    const val CONTENT_TYPE_OPENXML = "application/vnd.openxmlformats-officedocument"

    /** Represents image content type (e.g., "image/jpeg"). */
    const val CONTENT_TYPE_IMAGE = "image/"

    /** Represents a generic binary stream content type, often used for unknown file types. */
    const val CONTENT_TYPE_OCTET_STREAM = "application/octet-stream"

    // Messages

    /** Message indicating that a file has been detected and the download process has started. */
    const val MESSAGE_FILE_DETECTED = "File detected and download started"

    /** Message indicating that a request has failed with an exception. */
    const val MESSAGE_REQUEST_FAILED = "Request failed with exception: "
}