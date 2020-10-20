package com.audioburst.library.interactors

import kotlin.test.Test
import kotlin.test.assertTrue

class CurrentAdsInteractorTest {

    @Test
    fun testWhenStorageReturnsEmptyList() {
        // GIVEN
        val interactor = CurrentAdsInteractor(
            playlistStorage = playlistStorageOf(
                currentAds = emptyList()
            )
        )

        // WHEN
        val returned = interactor()

        // THEN
        assertTrue(returned.isEmpty())
    }

    @Test
    fun testWhenStorageReturnsNotEmptyList() {
        // GIVEN
        val interactor = CurrentAdsInteractor(
            playlistStorage = playlistStorageOf(
                currentAds = listOf(
                    downloadedAdvertisementOf()
                )
            )
        )

        // WHEN
        val returned = interactor()

        // THEN
        assertTrue(returned.isNotEmpty())
    }
}