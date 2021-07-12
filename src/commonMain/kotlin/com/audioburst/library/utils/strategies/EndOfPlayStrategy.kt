package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*

/**
 * When URL received in currentState() function is the URL of the next Burst in Playlist and user reached 90% of playback of previous Burst
 */
internal class EndOfPlayStrategy : PlaybackEventStrategy<PlaybackEvent.EndOfPlay> {

    override fun check(input: AnalysisInput): PlaybackEvent.EndOfPlay? {
        val lastState = input.lastState() ?: return null
        val eventPayload = input.eventPayload(playbackState = lastState) ?: return null
        val indexOfCurrentBurst = input.indexOfCurrentBurst()
        val indexOfPreviousBurst = input.indexOfLastBurst()
        return when {
            indexOfCurrentBurst == -1 || indexOfPreviousBurst == -1 -> null
            indexOfCurrentBurst == indexOfPreviousBurst + 1 -> when {
                input.playlist.bursts[indexOfPreviousBurst].percentProgressOf(lastState) >= THRESHOLD -> PlaybackEvent.EndOfPlay(eventPayload)
                else -> null
            }
            else -> null
        }
    }

    companion object {
        private const val THRESHOLD = 90
    }
}
