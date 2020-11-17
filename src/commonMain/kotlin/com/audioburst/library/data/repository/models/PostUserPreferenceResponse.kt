package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class PostUserPreferenceResponse(
    val success: Boolean,
    val preferences: UserPreferenceResponse,
)

@Serializable
internal data class UserPreferenceResponse(
    val id: String,
    val userId: String,
    val location: String?,
    val sourceType: String,
    val preferences: List<PreferenceResponse>,
)

@Serializable
internal data class PreferenceResponse(
    val name: String,
    val source: String,
    val take: Int,
    val offer: Int,
    val keys: List<KeyResponse>,
)

@Serializable
internal data class KeyResponse(
    val key: String,
    val segCategory: String,
    val source: String,
    val sourceId: Int,
    val position: Int,
    val selected: Boolean,
)
