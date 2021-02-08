package com.audioburst.library.models

class BurstSource(
    val sourceName: String,
    val sourceType: String?,
    val showName: String,
    val durationFromStart: Duration,
    val audioUrl: String?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BurstSource

        if (sourceName != other.sourceName) return false
        if (sourceType != other.sourceType) return false
        if (showName != other.showName) return false
        if (durationFromStart != other.durationFromStart) return false
        if (audioUrl != other.audioUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceName.hashCode()
        result = 31 * result + (sourceType?.hashCode() ?: 0)
        result = 31 * result + showName.hashCode()
        result = 31 * result + durationFromStart.hashCode()
        result = 31 * result + (audioUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BurstSource(sourceName='$sourceName', sourceType=$sourceType, showName='$showName', durationFromStart=$durationFromStart, audioUrl=$audioUrl)"
    }
}
