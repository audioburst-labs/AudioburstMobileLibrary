package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EndOfPlayStrategyTest {

    private val strategy = EndOfPlayStrategy()

    private fun testWithPosition(
        lastStatePosition: Double,
        previousBurstDuration: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val firstBurstId = "firstBurstId"
        val secondAudioUrl = "secondAudioUrl"
        val secondBurstId = "secondBurstId"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = firstBurstId,
                    audioUrl = firstAudioUrl,
                    duration = previousBurstDuration.toDuration(DurationUnit.Seconds)
                ),
                burstOf(
                    id = secondBurstId,
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
                advertisements = emptyList()
            )
        )

        // THEN
        if (isValid) {
            require(playbackEvent is PlaybackEvent.EndOfPlay)
            assertEquals(firstBurstId, playbackEvent.eventPayload.burst.id)
        } else {
            assertTrue(playbackEvent == null)
        }
    }

    @Test
    fun testDifferentPositions() {
        testWithPosition(
            lastStatePosition = 90.0,
            previousBurstDuration = 100.0,
            isValid = true,
        )

        testWithPosition(
            lastStatePosition = 95.0,
            previousBurstDuration = 100.0,
            isValid = true,
        )

        testWithPosition(
            lastStatePosition = 30.0,
            previousBurstDuration = 100.0,
            isValid = false,
        )

        testWithPosition(
            lastStatePosition = 30.0,
            previousBurstDuration = 0.0,
            isValid = false,
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
                advertisements = emptyList()
            )
        )

        // THEN
        assertEquals(null, playbackEvent)
    }
}
