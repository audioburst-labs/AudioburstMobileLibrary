package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponse(
    val ABUserID: String,
    val AppName: String,
    val EntryDate: String,
    val IP: String,
    val ExternalUserId: String
)
