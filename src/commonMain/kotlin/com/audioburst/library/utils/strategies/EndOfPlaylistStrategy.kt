package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the URL of the last Burst in Playlist and user reached more than 90% of playback
 */
internal class EndOfPlaylistStrategy : PlaybackEventStrategy<PlaybackEvent.EndOfPlaylist> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.EndOfPlaylist? {
        val eventPayload = input.currentEventPayload() ?: return null
        val indexOfBurst = input.indexOfCurrentBurst()
        return if (indexOfBurst == input.playlist.bursts.lastIndex) {
            val lastBurst = input.playlist.bursts.last()
            if (lastBurst.percentProgressOf(input.currentState) >= THRESHOLD) {
                PlaybackEvent.EndOfPlaylist(eventPayload)
            } else {
                null
            }
        } else {
            null
        }
    }

    companion object {
        private const val THRESHOLD = 90
    }
}
