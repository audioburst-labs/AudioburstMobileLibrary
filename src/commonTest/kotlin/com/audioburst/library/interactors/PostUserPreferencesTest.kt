package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.userPreferenceOf
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.models.UserPreferences
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostUserPreferencesTest {

    private fun interactor(
        postUserPreferencesReturns: Resource<UserPreferences>,
        getUserReturns: Resource<User>,
    ): PostUserPreferences =
        PostUserPreferences(
            getUser = getUserOf(getUserReturns),
            personalPlaylistRepository = personalPlaylistRepositoryOf(
                MockPersonalPlaylistRepository.Returns(
                    postUserPreferences = postUserPreferencesReturns
                )
            )
        )

    @Test
    fun testWhenGetUserReturnsError()= runTest {
        // GIVEN
        val userPreferences = userPreferenceOf()
        val getUserReturns = resourceErrorOf()
        val postUserPreferencesReturns = Resource.Data(userPreferenceOf())

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            postUserPreferencesReturns = postUserPreferencesReturns
        )(userPreferences)

        // THEN
        assertTrue(result is Result.Error)
    }

    @Test
    fun testWhenGetUserReturnsDataAndInteractorReturnsError()= runTest {
        // GIVEN
        val userPreferences = userPreferenceOf()
        val getUserReturns = Resource.Data(userOf())
        val postUserPreferencesReturns = resourceErrorOf()

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            postUserPreferencesReturns = postUserPreferencesReturns
        )(userPreferences)

        // THEN
        assertTrue(result is Result.Error)
    }

    @Test
    fun testWhenGetUserReturnsDataAndInteractorReturnsData()= runTest {
        // GIVEN
        val userPreferences = userPreferenceOf()
        val getUserReturns = Resource.Data(userOf())
        val userPreferencesToReturn = userPreferenceOf()
        val postUserPreferencesReturns = Resource.Data(userPreferencesToReturn)

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            postUserPreferencesReturns = postUserPreferencesReturns
        )(userPreferences)

        // THEN
        require(result is Result.Data)
        assertEquals(result.value, userPreferencesToReturn)
    }
}
