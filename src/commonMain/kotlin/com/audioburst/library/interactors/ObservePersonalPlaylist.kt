package com.audioburst.library.interactors

import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.Result
import com.audioburst.library.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class ObservePersonalPlaylist(
    private val personalPlaylistRepository: PersonalPlaylistRepository,
    private val userStorage: UserStorage,
    private val requestPlaylistAsync: RequestPlaylistAsync,
) {

    operator fun invoke(): Flow<Result<PendingPlaylist>> {
        if (userStorage.selectedKeysCount == 0) {
            Logger.w("NoKeysSelected")
            return flowOf(Result.Error(LibraryError.NoKeysSelected))
        }
        return requestPlaylistAsync(personalPlaylistRepository::getPersonalPlaylistQueryId)
    }
}
