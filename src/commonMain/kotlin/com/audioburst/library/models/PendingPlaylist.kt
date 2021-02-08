package com.audioburst.library.models

class PendingPlaylist(
    val isReady: Boolean,
    val playlist: Playlist,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PendingPlaylist

        if (isReady != other.isReady) return false
        if (playlist != other.playlist) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isReady.hashCode()
        result = 31 * result + playlist.hashCode()
        return result
    }

    override fun toString(): String {
        return "PendingPlaylist(isReady=$isReady, playlist=$playlist)"
    }
}