package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.*
import com.audioburst.library.models.*
import com.audioburst.library.utils.Queue
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaybackEventStrategyInputTest {

    @Test
    fun testLastStateWhenPreviousStatesAreEmpty() {
        // GIVEN
        val input = inputOf()

        // WHEN
        val lastState = input.lastState()

        // THEN
        assertTrue(lastState == null)
    }

    @Test
    fun testLastStateWhenPreviousStatesAreNotEmpty() {
        // GIVEN
        val playbackState = playbackStateOf()
        val input = inputOf(
            previousStates = fixedQueueOf(
                items = arrayOf(playbackState)
            )
        )

        // WHEN
        val lastState = input.lastState()

        // THEN
        assertEquals(lastState, playbackState)
    }

    @Test
    fun testIndexOfCurrentBurstWhenBurstDoesntContainsCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf()
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlistOf(
                bursts = listOf(burst)
            )
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(-1, indexOfCurrentBurst)
    }

    @Test
    fun testIndexOfCurrentBurstWhenBurstAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            audioUrl = currentUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfCurrentBurst)
    }

    @Test
    fun testIndexOfCurrentBurstWhenBurstStreamUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            streamUrl = currentUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfCurrentBurst)
    }

    @Test
    fun testIndexOfCurrentBurstWhenBurstSourceAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            source = burstSourceOf(
                audioUrl = currentUrl
            )
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfCurrentBurst)
    }

    @Test
    fun testIndexOfCurrentBurstWhenThereIsAnAdvertisementThatAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val adDownloadUrl = "adDownloadUrl"
        val burst = burstOf(
            adUrl = adDownloadUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = previousUrl
            ),
            playlist = playlist,
            advertisements = listOf(
                downloadedAdvertisementOf(
                    downloadUrl = Url(adDownloadUrl),
                    advertisement = advertisementOf(
                        audioURL = previousUrl
                    )
                )
            )
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfCurrentBurst)
    }

    @Test
    fun testIndexOfLastBurstWhenPreviousStatesAreEmpty() {
        // GIVEN
        val burst = burstOf()
        val input = inputOf(
            previousStates = fixedQueueOf(),
            playlist = playlistOf(
                bursts = listOf(burst)
            )
        )

        // WHEN
        val indexOfLastBurst = input.indexOfLastBurst()

        // THEN
        assertEquals(-1, indexOfLastBurst)
    }

    @Test
    fun testIndexOfLastBurstWhenBurstDoesntContainsPreviouslyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val burst = burstOf()
        val input = inputOf(
            previousStates = fixedQueueOf(
                items = arrayOf(
                    playbackStateOf(
                        url = previousUrl
                    )
                )
            ),
            playlist = playlistOf(
                bursts = listOf(burst)
            )
        )

        // WHEN
        val indexOfLastBurst = input.indexOfLastBurst()

        // THEN
        assertEquals(-1, indexOfLastBurst)
    }

    @Test
    fun testIndexOfLastBurstWhenBurstAudioUrlMatchesPreviouslyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val burst = burstOf(
            streamUrl = previousUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            previousStates = fixedQueueOf(
                items = arrayOf(
                    playbackStateOf(
                        url = previousUrl
                    )
                )
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfLastBurst = input.indexOfLastBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfLastBurst)
    }

    @Test
    fun testIndexOfLastBurstWhenBurstSourceAudioUrlMatchesPreviouslyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val burst = burstOf(
            source = burstSourceOf(
                audioUrl = previousUrl
            )
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            previousStates = fixedQueueOf(
                items = arrayOf(
                    playbackStateOf(
                        url = previousUrl
                    )
                )
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfLastBurst = input.indexOfLastBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfLastBurst)
    }

    @Test
    fun testIndexOfLastBurstWhenThereIsAnAdvertisementThatAudioUrlMatchesPreviouslyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val adDownloadUrl = "adDownloadUrl"
        val burst = burstOf(
            adUrl = adDownloadUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            previousStates = fixedQueueOf(
                items = arrayOf(
                    playbackStateOf(
                        url = previousUrl
                    )
                )
            ),
            playlist = playlist,
            advertisements = listOf(
                downloadedAdvertisementOf(
                    downloadUrl = Url(adDownloadUrl),
                    advertisement = advertisementOf(
                        audioURL = previousUrl
                    )
                )
            )
        )

        // WHEN
        val indexOfLastBurst = input.indexOfLastBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfLastBurst)
    }

    @Test
    fun testCurrentBurstWhenBurstDoesntContainsCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf()
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlistOf(
                bursts = listOf(burst)
            )
        )

        // WHEN
        val currentBurst = input.currentBurst()

        // THEN
        assertEquals(null, currentBurst)
    }

    @Test
    fun testCurrentBurstWhenBurstAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            audioUrl = currentUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val currentBurst = input.currentBurst()

        // THEN
        assertEquals(burst, currentBurst)
    }

    @Test
    fun testCurrentBurstWhenBurstStreamUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            streamUrl = currentUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val indexOfCurrentBurst = input.indexOfCurrentBurst()

        // THEN
        assertEquals(playlist.bursts.indexOf(burst), indexOfCurrentBurst)
    }

    @Test
    fun testCurrentBurstWhenBurstSourceAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val currentUrl = "currentUrl"
        val burst = burstOf(
            source = burstSourceOf(
                audioUrl = currentUrl
            )
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = currentUrl
            ),
            playlist = playlist
        )

        // WHEN
        val currentBurst = input.currentBurst()

        // THEN
        assertEquals(burst, currentBurst)
    }

    @Test
    fun testCurrentBurstWhenThereIsAnAdvertisementThatAudioUrlMatchesCurrentlyPlayedUrl() {
        // GIVEN
        val previousUrl = "previousUrl"
        val adDownloadUrl = "adDownloadUrl"
        val burst = burstOf(
            adUrl = adDownloadUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = previousUrl
            ),
            playlist = playlist,
            advertisements = listOf(
                downloadedAdvertisementOf(
                    downloadUrl = Url(adDownloadUrl),
                    advertisement = advertisementOf(
                        audioURL = previousUrl
                    )
                )
            )
        )

        // WHEN
        val currentBurst = input.currentBurst()

        // THEN
        assertEquals(burst, currentBurst)
    }

    @Test
    fun testEventPayloadWhenPlaylistDoesntContainACurrentUrl() {
        // GIVEN
        val burst = burstOf()
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = "url"
            ),
            playlist = playlist,
        )

        // WHEN
        val eventPayload = input.currentEventPayload()

        // THEN
        assertTrue(eventPayload == null)
    }

    @Test
    fun testEventPayloadWhenPlaylistContainsACurrentUrl() {
        // GIVEN
        val audioUrl = "audioUrl"
        val burst = burstOf(
            audioUrl = audioUrl
        )
        val playlist = playlistOf(
            bursts = listOf(burst)
        )
        val input = inputOf(
            currentState = playbackStateOf(
                url = audioUrl
            ),
            playlist = playlist,
        )

        // WHEN
        val eventPayload = input.currentEventPayload()

        // THEN
        assertTrue(eventPayload != null)
    }
}

internal fun inputOf(
    playlist: Playlist = playlistOf(),
    currentState: InternalPlaybackState = playbackStateOf(),
    previousStates: Queue<InternalPlaybackState> = fixedQueueOf(10),
    advertisements: List<DownloadedAdvertisement> = listOf(),
): AnalysisInput =
    AnalysisInput(
        playlist = playlist,
        currentState = currentState,
        previousStates = previousStates,
        advertisements = advertisements,
    )
