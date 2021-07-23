package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal class AppSettingsResponse(
    val preferences: List<PreferenceImageResponse>,
    val texts: Texts,
)

@Serializable
internal class PreferenceImageResponse(
    val name: String,
    val image: String,
)

@Serializable
class Texts(
    val share: Share,
)

@Serializable
class Share(
    val burst: String,
    val playlist: String,
)