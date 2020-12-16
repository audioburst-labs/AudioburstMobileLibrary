package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.UserResponse
import com.audioburst.library.models.User

internal class UserResponseToUserMapper {

    fun map(from: UserResponse): User = User(from.ABUserID)
}
