package com.audioburst.library.models

data class Playlist(
    val query: String,
    val bursts: List<Burst>,
    val playerSessionId: PlayerSessionId,
)
