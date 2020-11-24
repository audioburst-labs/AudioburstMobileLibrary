package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import com.audioburst.library.utils.UuidFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserInteractorTest {

    private fun interactor(
        userRepositoryReturns: Resource<User>,
        userStorage: UserStorage,
        uuidFactory: UuidFactory,
    ): GetUserInteractor =
        GetUserInteractor(
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    registerUser = userRepositoryReturns
                )
            ),
            userStorage = userStorage,
            uuidFactory = uuidFactory
        )

    @Test
    fun testIfThereAreNoUserCredentialsInStorageAndRepositoryReturnsDataThenDataIsReturned() = runTest {
        // GIVEN
        val userRepositoryReturns = Resource.Data(userOf())
        val userStorage = userStorageOf(userId = null)
        val uuidFactory = uuidFactoryOf()

        // WHEN
        val resource = interactor(
            userRepositoryReturns = userRepositoryReturns,
            userStorage = userStorage,
            uuidFactory = uuidFactory,
        )()

        // THEN
        assertTrue(resource is Resource.Data)
    }

    @Test
    fun testIfThereAreNoUserCredentialsInStorageAndRepositoryReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val userRepositoryReturns = resourceErrorOf()
        val userStorage = userStorageOf(userId = null)
        val uuidFactory = uuidFactoryOf()

        // WHEN
        val resource = interactor(
            userRepositoryReturns = userRepositoryReturns,
            userStorage = userStorage,
            uuidFactory = uuidFactory,
        )()

        // THEN
        assertTrue(resource is Resource.Error)
    }

    @Test
    fun testIfThereAreUserCredentialsInStorageAndRepositoryReturnsErrorThenUserIsReturned() = runTest {
        // GIVEN
        val userRepositoryReturns = resourceErrorOf()
        val userId = "userId"
        val userStorage = userStorageOf(userId = userId)
        val uuidFactory = uuidFactoryOf()

        // WHEN
        val resource = interactor(
            userRepositoryReturns = userRepositoryReturns,
            userStorage = userStorage,
            uuidFactory = uuidFactory,
        )()

        // THEN
        require(resource is Resource.Data)
        assertEquals(resource.result.userId, userId)
    }

    @Test
    fun testIfUserCredentialsAreSavedInStorageAfterRegisteringNewAccount() = runTest {
        // GIVEN
        val userId = "userId"
        val userRepositoryReturns = Resource.Data(
            userOf(userId = userId)
        )
        val userStorage = userStorageOf(userId = null)
        val uuidFactory = uuidFactoryOf()

        // WHEN
        val resource = interactor(
            userRepositoryReturns = userRepositoryReturns,
            userStorage = userStorage,
            uuidFactory = uuidFactory,
        )()

        // THEN
        require(resource is Resource.Data)
        assertEquals(userStorage.userId, userId)
    }
}
