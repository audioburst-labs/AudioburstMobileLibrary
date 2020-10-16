package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.models.Advertisement
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetAdDataTest {

    private fun interactor(userRepositoryReturns: Resource<Advertisement>): GetAdData =
        GetAdData(
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    getAdData = userRepositoryReturns
                )
            )
    )

    @Test
    fun testIfResourceDataIsReturnedWhenRepositoryReturnsThat() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(advertisementOf())
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        assertTrue(resource is Resource.Data)
    }

    @Test
    fun testIfWhenAdUrlIsNullAndResourceDataIsReturnedFromRepositoryThenInteractorReturnsNull() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(advertisementOf())
        val burst = burstOf(adUrl = null)

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        require(resource is Resource.Error)
        require(resource.errorType is ErrorType.UnexpectedException)
    }

    @Test
    fun testIfWhenAdUrlIsNotNullAndResourceErrorIsReturnedFromRepositoryThenInteractorReturnsError() = runTest {
        // GIVEN
        val userRepositoryReturns = resourceErrorOf()
        val burst = burstOf(adUrl = "google.com")

        // WHEN
        val resource = interactor(userRepositoryReturns)(burst)

        // THEN
        assertTrue(resource is Resource.Error)
    }
}
