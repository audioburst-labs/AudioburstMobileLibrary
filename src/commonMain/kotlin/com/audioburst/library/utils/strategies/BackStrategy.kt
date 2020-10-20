package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the URL of the previous Burst in Playlist
 */
internal class BackStrategy : PlaybackEventStrategy<PlaybackEvent.Back> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.Back? {
        val eventPayload = input.currentEventPayload() ?: return null
        input.lastState() ?: return null
        val indexOfCurrentBurst = input.indexOfCurrentBurst()
        val indexOfPreviousBurst = input.indexOfLastBurst()
        return when {
            indexOfCurrentBurst == -1 || indexOfPreviousBurst == -1 -> null
            indexOfCurrentBurst < indexOfPreviousBurst -> PlaybackEvent.Back(eventPayload)
            else -> null
        }
    }
}
