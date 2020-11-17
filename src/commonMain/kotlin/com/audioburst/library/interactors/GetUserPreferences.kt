package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.Result
import com.audioburst.library.models.UserPreferences

internal class GetUserPreferences(
    private val getUser: GetUser,
    private val personalPlaylistRepository: PersonalPlaylistRepository
) {

    suspend operator fun invoke(): Result<UserPreferences> =
        getUser().then { user ->
            personalPlaylistRepository.getUserPreferences(user)
        }.asResult()
}
