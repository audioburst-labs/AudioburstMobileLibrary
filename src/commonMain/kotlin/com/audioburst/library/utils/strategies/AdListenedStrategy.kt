package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent

/**
 * When URL received in currentState() function is the URL of the advertisement and user's playback position passed one of the positions that is included in
 * advertisement's ReportingData. The tolerance is +/- 2 seconds
 */
internal class AdListenedStrategy : PlaybackEventStrategy<PlaybackEvent.AdListened> {

    override fun check(input: AnalysisInput): PlaybackEvent.AdListened? {
        val eventPayload = input.currentEventPayload() ?: return null
        val burst = input.currentBurst() ?: return null
        val advertisement = input.advertisements.firstOrNull { it.downloadUrl.value == burst.adUrl } ?: return null
        return if (advertisement.advertisement.audioURL == input.currentState.url) {
            advertisement.advertisement.reportingData.firstOrNull {
                val expectedRange = it.position - THRESHOLD..it.position + THRESHOLD
                expectedRange.contains(input.currentState.position.seconds)
            }?.let {
                PlaybackEvent.AdListened(
                    advertisement = advertisement.advertisement,
                    reportingData = it,
                    eventPayload = eventPayload.copy(currentPlayBackPosition = it.position.toDuration(DurationUnit.Seconds)),
                )
            }
        } else {
            null
        }
    }

    companion object {
        private const val THRESHOLD = 2
    }
}
