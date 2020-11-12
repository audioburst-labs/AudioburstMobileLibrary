package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class AdvertisementResponse(
    val adData: AdvertisementDataResponse,
    val type: String,
    val url: String?,
)

@Serializable
internal data class AdvertisementDataResponse(
    val audioURL: String,
    val duration: Double,
    val id: String,
    val pixelURL: String,
    val position: String,
    val provider: String,
    val reportingPixelURLs: List<AdvertisementReportingPixelURLResponse>,
)

@Serializable
internal data class AdvertisementReportingPixelURLResponse(
    val pixelURL: String,
    val positionText: String,
    val position: Double,
)
