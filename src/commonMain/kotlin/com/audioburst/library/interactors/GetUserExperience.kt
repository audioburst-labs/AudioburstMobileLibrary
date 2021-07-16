package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.models.Result
import com.audioburst.library.models.UserExperience

internal class GetUserExperience(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(applicationKey: String, experienceId: String): Result<UserExperience> =
        userRepository.getUserExperience(applicationKey = applicationKey, experienceId = experienceId).asResult()
}
