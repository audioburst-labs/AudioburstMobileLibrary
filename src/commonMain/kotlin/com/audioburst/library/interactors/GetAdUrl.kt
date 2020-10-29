package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.map
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.models.Burst
import com.audioburst.library.models.Result
import com.audioburst.library.models.Url

internal class GetAdUrl(
    private val userRepository: UserRepository,
) {

    internal suspend operator fun invoke(burst: Burst): Result<String> =
        burst.adUrl?.let { adUrl ->
            userRepository.getAdData(
                adUrl = Url(adUrl)
            )
        }?.map { it.audioURL }?.asResult() ?: Result.Error(Result.Error.Type.AdUrlNotFound)
}
