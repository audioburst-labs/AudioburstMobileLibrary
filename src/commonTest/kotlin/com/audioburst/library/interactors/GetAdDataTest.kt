package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.storage.InMemoryPlaylistStorage
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.PromoteData
import com.audioburst.library.models.Result
import com.audioburst.library.models.errorType
import com.audioburst.library.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAdDataTest {

    private val playlistStorage = InMemoryPlaylistStorage()
    private fun interactor(userRepositoryReturns: Resource<PromoteData>): GetAdUrl =
        GetAdUrl(
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    getPromoteData = userRepositoryReturns
                )
            ),
            playlistStorage = playlistStorage,
    )

    @AfterTest
    fun clear() {
        playlistStorage.clear()
    }

    @Test
    fun testIfResourceDataIsReturnedWhenRepositoryReturnsThat() = runTest {
        // GIVEN
        val burstUrl = "audioUrl"
        val userRepositoryReturns = Resource.Data(
            promoteDataOf(
                advertisement = advertisementOf(burstUrl = burstUrl)
            )
        )
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Data)
        assertEquals(resource.value, burstUrl)
    }

    @Test
    fun testIfResourceErrorIsReturnedWhenRepositoryReturnsDataWithNullBurstUrl() = runTest {
        // GIVEN
        val burstUrl = null
        val userRepositoryReturns = Resource.Data(
            promoteDataOf(
                advertisement = advertisementOf(burstUrl = burstUrl)
            )
        )
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Error)
        require(resource.errorType == LibraryError.AdUrlNotFound)
    }

    @Test
    fun testIfWhenAdUrlIsNullAndResourceDataIsReturnedFromRepositoryThenInteractorReturnsNull() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(promoteDataOf(advertisement = advertisementOf()))
        val burst = burstOf(adUrl = null)

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Error)
        require(resource.errorType == LibraryError.AdUrlNotFound)
    }

    @Test
    fun testIfWhenAdUrlIsNotNullAndResourceErrorIsReturnedFromRepositoryThenInteractorReturnsError() = runTest {
        // GIVEN
        val userRepositoryReturns = resourceErrorOf()
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfAdIsBeingSavedWhenUserRepositoryReturnsResourceDataAndPlaylistStorageContainsACorrectPlaylist() = runTest {
        // GIVEN
        val burstUrl = "burstUrl"
        val userRepositoryReturns = Resource.Data(
            promoteDataOf(
                advertisement = advertisementOf(burstUrl = burstUrl)
            )
        )
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        playlistStorage.setPlaylist(playlistOf(bursts = listOf(burst)))
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Data)
        assertTrue(playlistStorage.currentAds.isNotEmpty())
    }

    @Test
    fun testIfAdIsNotBeingSavedWhenUserRepositoryReturnsResourceDataButBurstUrlIsNullAndPlaylistStorageContainsACorrectPlaylist() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(
            promoteDataOf(
                advertisement = advertisementOf(burstUrl = null)
            )
        )
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        playlistStorage.setPlaylist(playlistOf(bursts = listOf(burst)))
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Error)
        assertTrue(playlistStorage.currentAds.isEmpty())
    }
}
