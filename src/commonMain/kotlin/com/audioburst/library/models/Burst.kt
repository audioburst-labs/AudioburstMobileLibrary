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
}
