package com.audioburst.library.models

internal data class Advertisement(
    val id: String,
    val type: String,
    val audioURL: String,
    val duration: Duration,
    val pixelURL: String,
    val position: String,
    val provider: String,
    val reportingData: List<ReportingData>
)

internal data class ReportingData(
    val url: String,
    val text: String,
    val position: Double
)

internal data class DownloadedAdvertisement(
    val downloadUrl: Url,
    val advertisement: Advertisement,
)
