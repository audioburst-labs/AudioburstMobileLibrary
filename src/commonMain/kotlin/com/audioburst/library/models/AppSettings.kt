package com.audioburst.library.models

internal data class AppSettings(
    val preferenceImages: List<PreferenceImage>,
)

internal data class PreferenceImage(
    val name: String,
    val imageUrl: Url,
)