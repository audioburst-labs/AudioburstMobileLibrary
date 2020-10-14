package com.audioburst.library.models

internal data class AdvertisementEvent(
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
