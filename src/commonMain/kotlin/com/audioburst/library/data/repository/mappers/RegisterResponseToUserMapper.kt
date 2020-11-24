package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.RegisterResponse
import com.audioburst.library.models.User

internal class RegisterResponseToUserMapper {

    fun map(from: RegisterResponse): User = User(from.ABUserID)
}
