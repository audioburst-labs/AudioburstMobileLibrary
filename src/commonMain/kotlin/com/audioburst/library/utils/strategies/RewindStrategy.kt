package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the same as previous, but position is lower than previous position - 3
 */
internal class RewindStrategy : PlaybackEventStrategy<PlaybackEvent.Rewind> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.Rewind? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        return when (lastState.url) {
            input.currentState.url -> when {
                input.currentState.position < (lastState.position - THRESHOLD) -> PlaybackEvent.Rewind(eventPayload)
                else -> null
            }
            else -> null
        }
    }

    companion object {
        private const val THRESHOLD = 3
    }
}
