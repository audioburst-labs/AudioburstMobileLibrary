package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.userPreferenceOf
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.models.UserPreferences
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserPreferencesTest {

    private fun interactor(
        getUserPreferencesReturns: Resource<UserPreferences>,
        getUserReturns: Resource<User>,
    ): GetUserPreferences =
        GetUserPreferences(
            getUser = getUserOf(getUserReturns),
            personalPlaylistRepository = personalPlaylistRepositoryOf(
                MockPersonalPlaylistRepository.Returns(
                    getUserPreferences = getUserPreferencesReturns
                )
            ),
            updateSelectedKeysCount = UpdateSelectedKeysCount(userStorageOf())
        )

    @Test
    fun testWhenGetUserReturnsError()= runTest {
        // GIVEN
        val getUserReturns = resourceErrorOf()
        val getUserPreferencesReturns = Resource.Data(userPreferenceOf())

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getUserPreferencesReturns = getUserPreferencesReturns
        )()

        // THEN
        assertTrue(result is Result.Error)
    }

    @Test
    fun testWhenGetUserReturnsDataAndInteractorReturnsError()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getUserPreferencesReturns = resourceErrorOf()

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getUserPreferencesReturns = getUserPreferencesReturns
        )()

        // THEN
        assertTrue(result is Result.Error)
    }

    @Test
    fun testWhenGetUserReturnsDataAndInteractorReturnsData()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val userPreferencesToReturn = userPreferenceOf()
        val getUserPreferencesReturns = Resource.Data(userPreferencesToReturn)

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getUserPreferencesReturns = getUserPreferencesReturns
        )()

        // THEN
        require(result is Result.Data)
        assertEquals(result.value, userPreferencesToReturn)
    }
}
