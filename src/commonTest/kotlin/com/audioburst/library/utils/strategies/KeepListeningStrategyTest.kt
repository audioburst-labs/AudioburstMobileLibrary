package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.burstSourceOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class KeepListeningStrategyTest {

    private val strategy = KeepListeningStrategy()

    @Test
    fun testHappyPath() {
        // GIVEN
        val sourceAudioUrl = "sourceAudioUrl"
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl,
                    source = burstSourceOf(
                        audioUrl = sourceAudioUrl
                    )
                ),
            )
        )
        val currentState = playbackStateOf(
            url = sourceAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = audioUrl,
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
        assertTrue(playbackEvent is PlaybackEvent.KeepListening)
    }

    @Test
    fun testWhenPreviousAndCurrentStatesUrlIsTheSame() {
        // GIVEN
        val audioUrl = "audioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = audioUrl,
                ),
            )
        )
        val currentState = playbackStateOf(
            url = audioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = audioUrl,
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
    fun testWhenPlaylistIsEmpty() {
        // GIVEN
        val sourceAudioUrl = "sourceAudioUrl"
        val audioUrl = "audioUrl"
        val playlist = playlistOf()
        val currentState = playbackStateOf(
            url = audioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = sourceAudioUrl,
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
