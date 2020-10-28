package com.audioburst.library.utils

import com.audioburst.library.models.PlaybackState

fun interface PlaybackStateListener {

    fun getPlaybackState(): PlaybackState?
}
