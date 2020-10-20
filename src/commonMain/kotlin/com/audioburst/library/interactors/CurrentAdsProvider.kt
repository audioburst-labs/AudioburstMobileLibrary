package com.audioburst.library.interactors

import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.DownloadedAdvertisement

internal fun interface CurrentAdsProvider {

    operator fun invoke(): List<DownloadedAdvertisement>
}

internal class CurrentAdsInteractor(
    private val playlistStorage: PlaylistStorage
) : CurrentAdsProvider {

    override fun invoke(): List<DownloadedAdvertisement> = playlistStorage.currentAds
}
