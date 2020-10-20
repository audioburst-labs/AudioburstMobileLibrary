package com.audioburst.library.utils.strategies

import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function points to the URL of "source.audioURL" of the Burst that was previously played
 */
internal class KeepListeningStrategy : PlaybackEventStrategy<PlaybackEvent.KeepListening> {

    override fun check(input: PlaybackEventStrategy.Input): PlaybackEvent.KeepListening? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        val burst = input.currentBurst() ?: return null
        return if (input.currentState.url == burst.source.audioUrl && burst.isAudioUrl(lastState.url, input.advertisements)) {
            PlaybackEvent.KeepListening(eventPayload)
        } else {
            null
        }
    }
}
