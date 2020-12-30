package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ForwardStrategyTest {

    private val strategy = ForwardStrategy()

    @Test
    fun testWhenCurrentStateIsBiggerThanPreviousBy3seconds() {
        // GIVEN
        val audioUrl = "audioUrl"
        val position = 1.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = audioUrl,
            position = position + 4,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = audioUrl,
                    position = position
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
        assertTrue(playbackEvent is PlaybackEvent.Forward)
    }

    @Test
    fun testWhenCurrentStateIsBiggerThanPreviousBy2seconds() {
        // GIVEN
        val audioUrl = "audioUrl"
        val position = 1.0
        val playlist = playlistOf()
        val currentState = playbackStateOf(
            url = audioUrl,
            position = position + 3,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = audioUrl,
                    position = position
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
        assertEquals(null, playbackEvent)
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
