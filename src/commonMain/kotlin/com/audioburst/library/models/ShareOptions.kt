package com.audioburst.library.models

class ShareOptions(
    val burst: ShareData,
    val playlist: ShareData?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ShareOptions

        if (burst != other.burst) return false
        if (playlist != other.playlist) return false

        return true
    }

    override fun hashCode(): Int {
        var result = burst.hashCode()
        result = 31 * result + (playlist?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ShareOptions(burst=$burst, playlist=$playlist)"
    }
}