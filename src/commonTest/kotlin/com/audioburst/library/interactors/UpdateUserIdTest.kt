package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.InMemoryUserStorage
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateUserIdTest {

    private val userStorage = InMemoryUserStorage()
    private fun interactor(verifyUserIdReturns: Resource<User>): UpdateUserId =
        UpdateUserId(
            userStorage = userStorage,
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    verifyUserId = verifyUserIdReturns
                )
            )
        )

    @AfterTest
    fun clearStorage() {
        userStorage.userId = null
    }

    @Test
    fun testIfInteracorReturnsResourceDataTrueWhenGivenUserIdIsCorrect() = runTest {
        // GIVEN
        val verifyUserIdReturns = Resource.Data(userOf())

        // WHEN
        val result = interactor(verifyUserIdReturns = verifyUserIdReturns)("")

        // THEN
        require(result is Result.Data)
        assertTrue(result.value)
    }

    @Test
    fun testIfInteracorReturnsResourceDataFalseWhenApiIsReturningServerError() = runTest {
        // GIVEN
        val verifyUserIdReturns = resourceErrorOf(errorType = ErrorType.HttpError.InternalServerErrorException)

        // WHEN
        val result = interactor(verifyUserIdReturns = verifyUserIdReturns)("")

        // THEN
        require(result is Result.Data)
        assertTrue(!result.value)
    }

    @Test
    fun testIfInteracorReturnsResourceErrorWhenThereIsAnErrorCommunicatingTheApi() = runTest {
        // GIVEN
        val verifyUserIdReturns = resourceErrorOf(errorType = ErrorType.HttpError.BadRequestException)

        // WHEN
        val result = interactor(verifyUserIdReturns = verifyUserIdReturns)("")

        // THEN
        require(result is Result.Error)
    }

    @Test
    fun testIfUserIdIsGettingSavedIfItIsCorrect() = runTest {
        // GIVEN
        val userId = "userId"
        val verifyUserIdReturns = Resource.Data(userOf())

        // WHEN
        interactor(verifyUserIdReturns = verifyUserIdReturns)(userId)

        // THEN
        assertEquals(userStorage.userId, userId)
    }

    @Test
    fun testIfUserIdIsNotGettingSavedWhenThereIsErrorWhileCommunicatingWithTheApi() = runTest {
        // GIVEN
        val userId = "userId"
        val verifyUserIdReturns = resourceErrorOf()

        // WHEN
        interactor(verifyUserIdReturns = verifyUserIdReturns)(userId)

        // THEN
        assertEquals(userStorage.userId, null)
    }
}
