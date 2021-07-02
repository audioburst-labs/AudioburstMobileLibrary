package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*

/**
 * When URL received in currentState() function is the same as previous, but position is bigger than previous position + 4s
 */
internal class ForwardStrategy : PlaybackEventStrategy<PlaybackEvent.Forward> {

    override fun check(input: AnalysisInput): PlaybackEvent.Forward? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return when (lastState.url) {
            input.currentState.url -> when {
                input.currentState.position > (lastState.position + THRESHOLD) -> PlaybackEvent.Forward(eventPayload)
                else -> null
            }
            else -> null
        }
    }

    companion object {
        private val THRESHOLD = 4.0.toDuration(DurationUnit.Seconds)
    }
}
