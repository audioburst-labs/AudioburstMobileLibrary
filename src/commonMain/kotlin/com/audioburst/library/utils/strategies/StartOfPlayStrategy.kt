package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is different than URL we received previously
 */
internal class StartOfPlayStrategy : PlaybackEventStrategy<PlaybackEvent.StartOfPlay> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.StartOfPlay? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState()
        return when (lastState?.url) {
            input.currentState.url -> null
            else -> PlaybackEvent.StartOfPlay(eventPayload)
        }
    }
}
