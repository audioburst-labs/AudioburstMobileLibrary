package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.currentEventPayload
import com.audioburst.library.models.lastState

/**
 * When URL received in currentState() function is the same as previous, but position is within 0-5% of total time and smaller than the previous position
 */
internal class RepeatStrategy : PlaybackEventStrategy<PlaybackEvent.Repeat> {

    override fun check(input: AnalysisInput): PlaybackEvent.Repeat? {
        val eventPayload = input.currentEventPayload() ?: return null
        val lastState = input.lastState() ?: return null
        if (lastState.url != input.currentState.url) return null
        if (lastState.position.seconds < input.currentState.position.seconds) return null
        val burst = input.currentBurst() ?: return null
        if (burst.duration.milliseconds == 0.0) return null
        return if (burst.percentProgressOf(input.currentState) <= THRESHOLD) {
            PlaybackEvent.Repeat(eventPayload)
        } else {
            null
        }
    }

    companion object {
        private const val THRESHOLD = 5
    }
}
