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

internal class EndOfPlaylistStrategyTest {

    private val strategy = EndOfPlaylistStrategy()

    private fun testWithPosition(
        currentStatePosition: Double,
        lastBurstDuration: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl,
                    duration = lastBurstDuration.toDuration(DurationUnit.Seconds)
                )
            )
        )
        val currentState = playbackStateOf(
            url = audioUrl,
            position = currentStatePosition,
        )
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
        if (isValid) {
            assertTrue(playbackEvent != null)
        } else {
            assertTrue(playbackEvent == null)
        }
    }

    @Test
    fun testDifferentPositions() {
        testWithPosition(
            currentStatePosition = 90.0,
            lastBurstDuration = 100.0,
            isValid = true,
        )

        testWithPosition(
            currentStatePosition = 95.0,
            lastBurstDuration = 100.0,
            isValid = true,
        )

        testWithPosition(
            currentStatePosition = 30.0,
            lastBurstDuration = 100.0,
            isValid = false,
        )

        testWithPosition(
            currentStatePosition = 30.0,
            lastBurstDuration = 0.0,
            isValid = false,
        )
    }

    @Test
    fun testWhenBurstIsSecondToLast() {
        // GIVEN
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl,
                ),
                burstOf()
            )
        )
        val currentState = playbackStateOf(
            url = audioUrl,
        )
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
