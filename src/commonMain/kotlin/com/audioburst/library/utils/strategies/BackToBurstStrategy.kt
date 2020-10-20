package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When previously we received a "source.audioURL" of Burst that URL is currently received
 */
internal class BackToBurstStrategy : PlaybackEventStrategy<PlaybackEvent.BackToBurst> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.BackToBurst? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        val burst = input.currentBurst() ?: return null
        return if (burst.isAudioUrl(input.currentState.url, input.advertisements) && lastState.url == burst.source.audioUrl) {
            PlaybackEvent.BackToBurst(eventPayload)
        } else {
            null
        }
    }
}
