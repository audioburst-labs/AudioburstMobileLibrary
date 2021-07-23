package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AppSettingsResponse
import com.audioburst.library.data.repository.models.PreferenceImageResponse
import com.audioburst.library.models.AppSettings
import com.audioburst.library.models.PreferenceImage
import com.audioburst.library.models.ShareTexts
import com.audioburst.library.models.Url

internal class AppSettingsResponseToAppSettingsMapper {

    fun map(from: AppSettingsResponse): AppSettings =
        AppSettings(
            preferenceImages = from.preferences.map { it.toPreferenceImage() },
            shareTexts = ShareTexts(
                burst = from.texts.share.burst,
                playlist = from.texts.share.playlist,
            )
        )

    private fun PreferenceImageResponse.toPreferenceImage(): PreferenceImage =
        PreferenceImage(
            name = name,
            imageUrl = Url(image)
        )
}