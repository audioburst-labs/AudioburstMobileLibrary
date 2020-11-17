package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.Result
import com.audioburst.library.models.UserPreferences

internal class PostUserPreferences(
    private val getUser: GetUser,
    private val personalPlaylistRepository: PersonalPlaylistRepository
) {

    suspend operator fun invoke(userPreferences: UserPreferences): Result<UserPreferences> =
        getUser().then { user ->
            personalPlaylistRepository.postUserPreferences(user, userPreferences)
        }.asResult()
}
