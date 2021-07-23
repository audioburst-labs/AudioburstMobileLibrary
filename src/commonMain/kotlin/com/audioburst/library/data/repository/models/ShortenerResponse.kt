package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal class ShortenerResponse(
    val shortURL: String
)