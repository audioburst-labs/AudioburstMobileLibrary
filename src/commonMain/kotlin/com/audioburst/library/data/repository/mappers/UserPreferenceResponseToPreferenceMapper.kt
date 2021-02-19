package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.KeyResponse
import com.audioburst.library.data.repository.models.PreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.models.Key
import com.audioburst.library.models.Preference
import com.audioburst.library.models.PreferenceImage
import com.audioburst.library.models.UserPreferences

internal class UserPreferenceResponseToPreferenceMapper {

    fun map(from: UserPreferenceResponse, preferencesImages: List<PreferenceImage>): UserPreferences =
        UserPreferences(
            id = from.id,
            userId = from.userId,
            location = from.location,
            sourceType = from.sourceType,
            preferences = from.preferences.map { it.toPreference(preferencesImages) },
        )

    private fun PreferenceResponse.toPreference(preferencesImages: List<PreferenceImage>): Preference =
        Preference(
            name = name,
            source = source,
            take = take,
            offer = offer,
            keys = keys.map { it.toKey() },
            iconUrl = preferencesImages.firstOrNull { it.name == name }?.imageUrl?.value
        )

    private fun KeyResponse.toKey(): Key =
        Key(
            key = key,
            segCategory = segCategory,
            source = source,
            sourceId = sourceId,
            position = position,
            selected = selected,
        )
}
