package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.map
import com.audioburst.library.data.onData
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.Burst
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.Result
import com.audioburst.library.models.Url

internal class GetAdUrl(
    private val userRepository: UserRepository,
    private val playlistStorage: PlaylistStorage,
) {

    internal suspend operator fun invoke(burst: Burst): Result<String> =
        burst.adUrl?.let { adUrl ->
            userRepository.getAdData(
                adUrl = Url(adUrl)
            )
        }?.onData {
            playlistStorage.setAdvertisement(
                url = Url(burst.adUrl),
                advertisement = it
            )
        }?.map { it.burstUrl }?.asResult()?.let {
            when (it) {
                is Result.Data -> if (it.value == null) {
                    Result.Error(LibraryError.AdUrlNotFound)
                } else {
                    Result.Data(it.value)
                }
                is Result.Error -> it
            }
        } ?: Result.Error(LibraryError.AdUrlNotFound)
}
