package com.audioburst.library.interactors

import com.audioburst.library.models.Playlist
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrentPlaylistInteractorTest {

    @Test
    fun testWhenStorageReturnsPlaylist() {
        // GIVEN
        val playlist = playlistOf()
        val interactor = CurrentPlaylistInteractor(
            playlistStorage = playlistStorageOf(
                currentPlaylist = playlist
            )
        )

        // WHEN
        val returned = interactor()

        // THEN
        assertEquals(returned, playlist)
    }

    @Test
    fun testWhenStorageReturnsNull() {
        // GIVEN
        val playlist: Playlist? = null
        val interactor = CurrentPlaylistInteractor(
            playlistStorage = playlistStorageOf(
                currentPlaylist = playlist
            )
        )

        // WHEN
        val returned = interactor()

        // THEN
        assertEquals(returned, playlist)
    }
}