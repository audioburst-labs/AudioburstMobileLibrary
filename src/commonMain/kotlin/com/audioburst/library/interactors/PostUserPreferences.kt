package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.Result
import com.audioburst.library.models.UserPreferences
import com.audioburst.library.models.onData

internal class PostUserPreferences(
    private val getUser: GetUser,
    private val personalPlaylistRepository: PersonalPlaylistRepository,
    private val updateSelectedKeysCount: UpdateSelectedKeysCount,
) {

    suspend operator fun invoke(userPreferences: UserPreferences): Result<UserPreferences> =
        getUser().then { user ->
            personalPlaylistRepository.postUserPreferences(user, userPreferences)
        }.asResult().onData { updateSelectedKeysCount(it) }
}
