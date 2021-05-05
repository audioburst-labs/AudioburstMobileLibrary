package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*

/**
 * When the library is informed about click on the CteButton. Additionally, we are trying to guess whether something is playing now or no.
 */
internal class CtaClickStrategy {

    fun check(input: AnalysisInput, burstId: String): PlaybackEvent.CtaClick? {
        val burst = input.playlist.bursts.firstOrNull { it.id == burstId } ?: return null
        val ctaData = burst.ctaData ?: return null
        val lastState = input.lastState()
        val isPlaying = lastState?.let { input.currentState.occurrenceTime - lastState.occurrenceTime <= THRESHOLD } ?: false
        val eventPayload = input.currentEventPayload(burst = burst, isPlaying = isPlaying) ?: return null
        return PlaybackEvent.CtaClick(
            eventPayload = eventPayload,
            buttonText = ctaData.buttonText,
            url = ctaData.url,
        )
    }

    companion object {
        private val THRESHOLD = 2.0.toDuration(DurationUnit.Seconds)
    }
}