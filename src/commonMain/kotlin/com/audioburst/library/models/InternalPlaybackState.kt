package com.audioburst.library.models

class PlaybackState(
    val url: String,
    val positionMillis: Long,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlaybackState

        if (url != other.url) return false
        if (positionMillis != other.positionMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + positionMillis.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlaybackState(url='$url', positionMillis=$positionMillis)"
    }
}

internal data class InternalPlaybackState(
    val url: String,
    val position: Duration,
    val occurrenceTime: Long,
)
