package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.models.Advertisement
import com.audioburst.library.models.Burst
import com.audioburst.library.models.Url

internal class GetAdData(
    private val userRepository: UserRepository
) {

    internal suspend operator fun invoke(burst: Burst): Resource<Advertisement> =
        burst.adUrl?.let { adUrl ->
            userRepository.getAdData(
                adUrl = Url(adUrl)
            )
        } ?: Resource.Error(ErrorType.UnexpectedException(IllegalStateException("Burst adUrl cannot be null.")))
}
