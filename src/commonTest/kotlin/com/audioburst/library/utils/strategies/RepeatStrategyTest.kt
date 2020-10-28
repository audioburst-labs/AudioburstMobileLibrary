package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.models.toDuration
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class RepeatStrategyTest {

    private val strategy = RepeatStrategy()

    private fun testWithPositions(
        burstDuration: Double,
        currentStatePosition: Double,
        lastStatePosition: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl,
                    duration = burstDuration.toDuration(DurationUnit.Seconds)
                )
            )
        )
        val currentState = playbackStateOf(
            url = audioUrl,
            position = currentStatePosition,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = audioUrl,
                    position = lastStatePosition
                )
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            PlaybackEventStrategy.Input(
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
        testWithPositions(
            burstDuration = 30.0,
            currentStatePosition = 1.0,
            lastStatePosition = 28.0,
            isValid = true,
        )

        testWithPositions(
            burstDuration = 30.0,
            currentStatePosition = 1.5,
            lastStatePosition = 2.0,
            isValid = true,
        )

        testWithPositions(
            burstDuration = 30.0,
            currentStatePosition = 0.0,
            lastStatePosition = 2.0,
            isValid = true,
        )

        testWithPositions(
            burstDuration = 0.0,
            currentStatePosition = 0.0,
            lastStatePosition = 2.0,
            isValid = false,
        )

        testWithPositions(
            burstDuration = 30.0,
            currentStatePosition = 1.6,
            lastStatePosition = 2.0,
            isValid = false,
        )

        testWithPositions(
            burstDuration = 30.0,
            currentStatePosition = 1.6,
            lastStatePosition = 1.0,
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
            PlaybackEventStrategy.Input(
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
