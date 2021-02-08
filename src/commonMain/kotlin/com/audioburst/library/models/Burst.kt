package com.audioburst.library.models

data class Burst(
    val id: String,
    val title: String,
    val creationDate: String,
    val duration: Duration,
    val sourceName: String,
    val category: String?,
    val playlistId: Long,
    val showName: String,
    val streamUrl: String?,
    val audioUrl: String,
    val imageUrls: List<String>,
    val source: BurstSource,
    val shareUrl: String,
    val keywords: List<String>,
    internal val adUrl: String?,
) {
    val isAdAvailable: Boolean
        get() = adUrl != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Burst

        if (id != other.id) return false
        if (title != other.title) return false
        if (creationDate != other.creationDate) return false
        if (duration != other.duration) return false
        if (sourceName != other.sourceName) return false
        if (category != other.category) return false
        if (playlistId != other.playlistId) return false
        if (showName != other.showName) return false
        if (streamUrl != other.streamUrl) return false
        if (audioUrl != other.audioUrl) return false
        if (imageUrls != other.imageUrls) return false
        if (source != other.source) return false
        if (shareUrl != other.shareUrl) return false
        if (keywords != other.keywords) return false
        if (adUrl != other.adUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + sourceName.hashCode()
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + playlistId.hashCode()
        result = 31 * result + showName.hashCode()
        result = 31 * result + (streamUrl?.hashCode() ?: 0)
        result = 31 * result + audioUrl.hashCode()
        result = 31 * result + imageUrls.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + shareUrl.hashCode()
        result = 31 * result + keywords.hashCode()
        result = 31 * result + (adUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Burst(id='$id', title='$title', creationDate='$creationDate', duration=$duration, sourceName='$sourceName', category=$category, playlistId=$playlistId, showName='$showName', streamUrl=$streamUrl, audioUrl='$audioUrl', imageUrls=$imageUrls, source=$source, shareUrl='$shareUrl', keywords=$keywords, adUrl=$adUrl)"
    }
}
