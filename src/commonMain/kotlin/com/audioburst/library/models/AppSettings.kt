package com.audioburst.library.models

internal data class AppSettings(
    val preferenceImages: List<PreferenceImage>,
    val shareTexts: ShareTexts,
)

internal data class PreferenceImage(
    val name: String,
    val imageUrl: Url,
)

internal data class ShareTexts(
    val burst: String,
    val playlist: String,
)