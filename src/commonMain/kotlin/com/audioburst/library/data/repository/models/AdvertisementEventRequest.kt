package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class AdvertisementEventRequest(
    val id: String,
    val type: String,
    val pixelURL: String,
    val duration: String,
    val audioURL: String,
    val provider: String,
    val position: String,
    val positionText: String,
    val currentPosition: Double,
    val currentPixelURL: String
)
