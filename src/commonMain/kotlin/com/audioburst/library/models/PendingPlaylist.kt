package com.audioburst.library.models

data class PendingPlaylist(
    val isReady: Boolean,
    val playlist: Playlist,
)