package com.audioburst.library.models

data class PlaylistInfo(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    val url: String,
)
