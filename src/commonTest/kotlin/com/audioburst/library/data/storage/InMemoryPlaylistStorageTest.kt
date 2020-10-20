package com.audioburst.library.data.storage

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.Url
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue

class InMemoryPlaylistStorageTest {

    private val playlistStorage = InMemoryPlaylistStorage

    @AfterTest
    fun clear() {
        playlistStorage.clear()
    }

    @Test
    fun testIfSetPlaylistWithResourceDataIsWorking() {
        // GIVEN
        val playlist = playlistOf()

        // WHEN
        playlistStorage.setPlaylist(playlist)

        // THEN
        assertTrue(playlistStorage.currentPlaylist != null)
    }

    @Test
    fun testSetAdvertisementWhenPlaylistIsNull() {
        // GIVEN
        val url = Url("url")
        val advertisement = advertisementOf()

        // WHEN
        playlistStorage.setAdvertisement(
            url = url,
            advertisement = advertisement
        )

        // THEN
        assertTrue(playlistStorage.currentAds.isEmpty())
    }

    @Test
    fun testSetAdvertisementWhenPlaylistIsNotNull() {
        // GIVEN
        val url = Url("url")
        val advertisement = advertisementOf()
        val playlist = playlistOf()

        // WHEN
        playlistStorage.setPlaylist(playlist)
        playlistStorage.setAdvertisement(
            url = url,
            advertisement = advertisement
        )

        // THEN
        assertTrue(playlistStorage.currentAds.isEmpty())
    }

    @Test
    fun testSetAdvertisementWhenPlaylistIsNotNullAndContainsBurstWithAdUrl() {
        // GIVEN
        val url = Url("url")
        val advertisement = advertisementOf()
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    adUrl = url.value
                )
            )
        )

        // WHEN
        playlistStorage.setPlaylist(playlist)
        playlistStorage.setAdvertisement(
            url = url,
            advertisement = advertisement
        )

        // THEN
        assertTrue(playlistStorage.currentAds.isNotEmpty())
    }

    @Test
    fun testThatAdvertisementIsGettingClearedAfterWeSetNewPlaylist() {
        // GIVEN
        val url = Url("url")
        val advertisement = advertisementOf()
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    adUrl = url.value
                )
            )
        )

        // WHEN
        playlistStorage.setPlaylist(playlist)
        playlistStorage.setAdvertisement(
            url = url,
            advertisement = advertisement
        )
        playlistStorage.setPlaylist(playlistOf())

        // THEN
        assertTrue(playlistStorage.currentAds.isEmpty())
    }

    @Test
    fun testClear() {
        // GIVEN
        val url = Url("url")
        val advertisement = advertisementOf()
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    adUrl = url.value
                )
            )
        )

        // WHEN
        playlistStorage.setPlaylist(playlist)
        playlistStorage.setAdvertisement(
            url = url,
            advertisement = advertisement
        )
        playlistStorage.clear()

        // THEN
        assertTrue(playlistStorage.currentPlaylist == null)
        assertTrue(playlistStorage.currentAds.isEmpty())
    }
}
