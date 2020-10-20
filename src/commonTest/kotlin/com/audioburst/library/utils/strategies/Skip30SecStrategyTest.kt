package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.PlaybackState
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class Skip30SecStrategyTest {

    private val strategy = Skip30SecStrategy()

    private fun testWithPositions(
        currentStatePosition: Double,
        previousStatePosition: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl
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
                    position = previousStatePosition
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
            currentStatePosition = 30.0,
            previousStatePosition = 0.0,
            isValid = true
        )

        testWithPositions(
            currentStatePosition = 31.0,
            previousStatePosition = 0.0,
            isValid = true
        )

        testWithPositions(
            currentStatePosition = 32.0,
            previousStatePosition = 0.0,
            isValid = true
        )

        testWithPositions(
            currentStatePosition = 0.0,
            previousStatePosition = 33.0,
            isValid = false
        )

        testWithPositions(
            currentStatePosition = 20.0,
            previousStatePosition = 0.0,
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
            items = arrayOf<PlaybackState>()
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
