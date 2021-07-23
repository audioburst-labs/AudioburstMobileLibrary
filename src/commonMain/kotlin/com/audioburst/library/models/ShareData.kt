package com.audioburst.library.models

class ShareData(
    val title: String,
    val message: String,
    val url: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ShareData

        if (title != other.title) return false
        if (message != other.message) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

    override fun toString(): String {
        return "ShareData(title='$title', message='$message', url='$url')"
    }
}