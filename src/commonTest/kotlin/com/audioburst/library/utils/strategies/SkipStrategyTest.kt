package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.models.toDuration
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SkipStrategyTest {

    private val strategy = SkipStrategy()

    private fun testWithPosition(
        lastStatePosition: Double,
        previousBurstDuration: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val secondAudioUrl = "secondAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = firstAudioUrl,
                    duration = previousBurstDuration.toDuration(DurationUnit.Seconds)
                ),
                burstOf(
                    audioUrl = secondAudioUrl,
                )
            )
        )
        val currentState = playbackStateOf(
            url = secondAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = firstAudioUrl,
                    position = lastStatePosition
                )
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            )
        )

        // THEN
        if (isValid) {
            assertTrue(playbackEvent != null)
        } else {
            assertTrue(playbackEvent == null)
        }
    }

    @Test
    fun testDifferentPositions() {
        testWithPosition(
            lastStatePosition = 80.0,
            previousBurstDuration = 100.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 89.0,
            previousBurstDuration = 100.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 89.0,
            previousBurstDuration = 0.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 95.0,
            previousBurstDuration = 100.0,
            isValid = false
        )
    }

    @Test
    fun testWhenThereIsNoPreviousState() {
        // GIVEN
        val playlist = playlistOf()
        val currentState = playbackStateOf()
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf<InternalPlaybackState>()
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            )
        )

        // THEN
        assertEquals(null, playbackEvent)
    }
}
