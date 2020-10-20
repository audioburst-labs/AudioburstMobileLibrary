package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the same as previous, but position is within previous position - (27-33s)
 */
internal class Back30SecStrategy : PlaybackEventStrategy<PlaybackEvent.BackSec30> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.BackSec30? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return if (input.currentState.url == lastState.url) {
            val expectedDifferenceRange = 27.0..33.0
            val positionDifference = lastState.position - input.currentState.position
            if (expectedDifferenceRange.contains(positionDifference)) {
                PlaybackEvent.BackSec30(eventPayload)
            } else {
                null
            }
        } else {
            null
        }
    }
}
