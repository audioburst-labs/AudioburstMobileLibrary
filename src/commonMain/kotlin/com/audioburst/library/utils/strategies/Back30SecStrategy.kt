package com.audioburst.library.utils.strategies

import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.currentEventPayload
import com.audioburst.library.models.lastState

/**
 * When URL received in currentState() function is the same as previous, but position is within previous position - (27-33s)
 */
internal class Back30SecStrategy : PlaybackEventStrategy<PlaybackEvent.BackSec30> {

    override fun check(input: AnalysisInput): PlaybackEvent.BackSec30? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return if (input.currentState.url == lastState.url) {
            val expectedDifferenceRange = 27.0..33.0
            val positionDifference = lastState.position.seconds - input.currentState.position.seconds
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
