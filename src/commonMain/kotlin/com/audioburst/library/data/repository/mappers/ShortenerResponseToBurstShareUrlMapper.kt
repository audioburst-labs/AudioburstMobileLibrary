package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.ShortenerResponse
import com.audioburst.library.models.BurstShareUrl

internal class ShortenerResponseToBurstShareUrlMapper {

    fun map(from: ShortenerResponse): BurstShareUrl = BurstShareUrl(shortUrl = from.shortURL)
}