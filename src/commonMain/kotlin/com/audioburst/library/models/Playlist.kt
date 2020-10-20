package com.audioburst.library.models

data class Playlist(
    val id: Int,
    val name: String,
    val query: String,
    val bursts: List<Burst>,
    val playerSessionId: PlayerSessionId,
)
