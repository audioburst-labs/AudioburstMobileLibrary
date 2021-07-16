package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.models.Result
import com.audioburst.library.models.UserExperience
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserExperienceTest {

    private val applicationKey = "applicationKey"
    private val experienceId = "experienceId"

    private fun interactor(
        getUserExperience: Resource<UserExperience>,
    ): GetUserExperience =
        GetUserExperience(
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    getUserExperience = getUserExperience,
                )
            )
        )

    @Test
    fun `test when interactor returns error`()= runTest {
        // GIVEN
        val getUserExperience = Resource.Error(ErrorType.HttpError.InternalServerErrorException)

        // WHEN
        val result = interactor(
            getUserExperience = getUserExperience,
        )(applicationKey, experienceId)

        // THEN
        assertTrue(result is Result.Error)
    }

    @Test
    fun `test when interactor returns data`()= runTest {
        // GIVEN
        val userExperience = userExperienceOf()
        val getUserExperience = Resource.Data(userExperience)

        // WHEN
        val result = interactor(
            getUserExperience = getUserExperience,
        )(applicationKey, experienceId)

        // THEN
        require(result is Result.Data)
        assertEquals(userExperience, result.value)
    }
}
