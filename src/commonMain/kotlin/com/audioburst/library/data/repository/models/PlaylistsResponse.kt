package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class PlaylistsResponse(
    val section: String,
    val playlistId: Int,
    val playlistName: String,
    val description: String,
    val image: ImageResponse,
    val url: String,
)

@Serializable
internal data class ImageResponse(
    val url: String,
    val thumbnail: String,
    val svg: String,
    val square: String,
)
