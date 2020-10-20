package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.PlaybackState
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertTrue

internal class StartOfPlayStrategyTest {

    private val strategy = StartOfPlayStrategy()

    @Test
    fun testHappyPath() {
        // GIVEN
        val currentAudioUrl = "currentAudioUrl"
        val previousAudioUrl = "previousAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = currentAudioUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = currentAudioUrl
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = previousAudioUrl,
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
        assertTrue(playbackEvent is PlaybackEvent.StartOfPlay)
    }

    @Test
    fun testHappyPathWhenFirstUrl() {
        // GIVEN
        val currentAudioUrl = "currentAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = currentAudioUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = currentAudioUrl
        )
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
        assertTrue(playbackEvent is PlaybackEvent.StartOfPlay)
    }
}
