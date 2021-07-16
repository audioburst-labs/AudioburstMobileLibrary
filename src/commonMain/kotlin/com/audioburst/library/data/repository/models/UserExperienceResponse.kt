package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal class UserExperienceResponse(
    val userId: String,
    val id: String,
    val settingsId: String,
    val name: String,
    val playlist: Int,
    val playerSettings: PlayerSettingsResponse,
    val PlayerAction: String,
    val PlayerActionValue: String?,
)

@Serializable
internal class PlayerSettingsResponse(
    val mode: String,
    val autoplay: Boolean,
    val accentColor: String,
    val theme: String,
    val shuffle: Boolean?,
)