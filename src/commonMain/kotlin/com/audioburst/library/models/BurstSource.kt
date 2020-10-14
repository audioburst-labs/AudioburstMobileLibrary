package com.audioburst.library.models

data class BurstSource(
    val sourceName: String,
    val sourceType: String?,
    val showName: String,
    val durationFromStart: Duration,
    val audioUrl: String?
)
