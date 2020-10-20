package com.audioburst.library.interactors

import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.models.PlaybackEvent

internal fun interface PlaybackEventHandler {
    fun handle(playbackEvent: PlaybackEvent)
}

internal class PlaybackEventHandlerInteractor(
    private val userRepository: UserRepository
) : PlaybackEventHandler {
    // TODO: It's going to be implemented in ABU-693
    override fun handle(playbackEvent: PlaybackEvent) {
        println("AudioburstLibrary: handle: $playbackEvent")
    }
}
