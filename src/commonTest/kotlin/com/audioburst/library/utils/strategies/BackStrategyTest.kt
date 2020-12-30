package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BackStrategyTest {

    private val strategy = BackStrategy()

    @Test
    fun testWhenSwitchingToPrevious() {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val secondAudioUrl = "secondAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = firstAudioUrl
                ),
                burstOf(
                    audioUrl = secondAudioUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = firstAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = secondAudioUrl,
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
        assertTrue(playbackEvent is PlaybackEvent.Back)
    }

    @Test
    fun testWhenSwitchingToNext() {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val secondAudioUrl = "secondAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = secondAudioUrl
                ),
                burstOf(
                    audioUrl = firstAudioUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = firstAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = secondAudioUrl,
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
    fun testWhenUrlDoesntAppearOnPlaylist() {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val secondAudioUrl = "secondAudioUrl"
        val playlist = playlistOf()
        val currentState = playbackStateOf(
            url = firstAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = secondAudioUrl,
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
