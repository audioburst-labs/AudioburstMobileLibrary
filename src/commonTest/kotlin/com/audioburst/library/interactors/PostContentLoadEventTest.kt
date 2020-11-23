package com.audioburst.library.interactors

import com.audioburst.library.runTest
import com.audioburst.library.utils.timestampProviderOf
import kotlin.test.Test
import kotlin.test.assertTrue

class PostContentLoadEventTest {

    private val playbackEventHandler = MemorablePlaybackEventHandler()
    private val interactor = postContentLoadEventOf(
        playbackEventHandler = playbackEventHandler,
        timestampProvider = timestampProviderOf(),
    )

    @Test
    fun testIfEventIsSentWhenPlaylistContainsAtLeastOneBurst() = runTest {
        // GIVEN
        val playlist = playlistOf(
            bursts = listOf(
                burstOf()
            )
        )

        // WHEN
        interactor(playlist)

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isNotEmpty())
    }

    @Test
    fun testIfEventIsNotSentWhenPlaylistDoesntContainsEventOneBurst() = runTest {
        // GIVEN
        val playlist = playlistOf()

        // WHEN
        interactor(playlist)

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isEmpty())
    }
}
