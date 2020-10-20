package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the same as previous, but position is within previous position + (27-33s)
 */
internal class Skip30SecStrategy : PlaybackEventStrategy<PlaybackEvent.Skip30Sec> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.Skip30Sec? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return if (input.currentState.url == lastState.url) {
            val expectedDifferenceRange = 27.0..33.0
            val positionDifference = input.currentState.position - lastState.position
            if (expectedDifferenceRange.contains(positionDifference)) {
                PlaybackEvent.Skip30Sec(eventPayload)
            } else {
                null
            }
        } else {
            null
        }
    }
}
