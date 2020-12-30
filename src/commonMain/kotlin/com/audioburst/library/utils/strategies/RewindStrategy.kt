package com.audioburst.library.utils.strategies

import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.currentEventPayload
import com.audioburst.library.models.lastState

/**
 * When URL received in currentState() function is the same as previous, but position is lower than previous position - 3
 */
internal class RewindStrategy : PlaybackEventStrategy<PlaybackEvent.Rewind> {

    override fun check(input: AnalysisInput): PlaybackEvent.Rewind? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return when (lastState.url) {
            input.currentState.url -> when {
                input.currentState.position.seconds < (lastState.position.seconds - THRESHOLD) -> PlaybackEvent.Rewind(eventPayload)
                else -> null
            }
            else -> null
        }
    }

    companion object {
        private const val THRESHOLD = 3
    }
}
