package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.Advertisement
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.Result
import com.audioburst.library.models.errorType
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAdDataTest {

    private fun interactor(userRepositoryReturns: Resource<Advertisement>): GetAdUrl =
        GetAdUrl(
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    getAdData = userRepositoryReturns
                )
            )
    )

    @Test
    fun testIfResourceDataIsReturnedWhenRepositoryReturnsThat() = runTest {
        // GIVEN
        val audioUrl = "audioUrl"
        val userRepositoryReturns = Resource.Data(
            advertisementOf(
                audioURL = audioUrl
            )
        )
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Result.Data)
        assertEquals(resource.value, audioUrl)
    }

    @Test
    fun testIfWhenAdUrlIsNullAndResourceDataIsReturnedFromRepositoryThenInteractorReturnsNull() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(advertisementOf())
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
}
