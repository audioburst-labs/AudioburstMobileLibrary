package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.onData
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.User
import com.audioburst.library.utils.UuidFactory

internal interface GetUser {
    suspend operator fun invoke(): Resource<User>
}

internal class GetUserInteractor(
    private val uuidFactory: UuidFactory,
    private val userStorage: UserStorage,
    private val userRepository: UserRepository,
) : GetUser {

    override suspend operator fun invoke(): Resource<User> {
        val userId = userStorage.userId
        return if (userId != null) {
            Resource.Data(
                User(userId)
            )
        } else {
            userRepository.registerUser(uuidFactory.getUuid()).onData {
                userStorage.userId = it.userId
            }
        }
    }
}
