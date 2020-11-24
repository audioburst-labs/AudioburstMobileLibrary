package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.data.toLibraryError
import com.audioburst.library.models.Result

internal class UpdateUserId(
    private val userStorage: UserStorage,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(userId: String): Result<Boolean> =
        when (val resource = userRepository.verifyUserId(userId)) {
            is Resource.Data -> {
                userStorage.userId = userId
                Result.Data(true)
            }
            is Resource.Error -> when (resource.errorType) {
                ErrorType.HttpError.InternalServerErrorException -> Result.Data(false)
                else -> Result.Error(resource.errorType.toLibraryError())
            }
        }
}
