package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class AppSettingsResponse(
    val preferences: List<PreferenceImageResponse>,
)

@Serializable
internal data class PreferenceImageResponse(
    val name: String,
    val image: String,
)