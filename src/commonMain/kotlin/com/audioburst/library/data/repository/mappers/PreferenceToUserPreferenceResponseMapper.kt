package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.KeyResponse
import com.audioburst.library.data.repository.models.PreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.models.Preference
import com.audioburst.library.models.UserPreferences
import com.audioburst.library.models.Key

internal class PreferenceToUserPreferenceResponseMapper {

    fun map(from: UserPreferences): UserPreferenceResponse =
        UserPreferenceResponse(
            id = from.id,
            userId = from.userId,
            location = from.location,
            sourceType = from.sourceType,
            preferences = from.preferences.map { it.toPreferenceResponse() },
        )

    private fun Preference.toPreferenceResponse(): PreferenceResponse =
        PreferenceResponse(
            name = name,
            source = source,
            take = take,
            offer = offer,
            keys = keys.map { it.toKeyRequestResponse() },
        )

    private fun Key.toKeyRequestResponse(): KeyResponse =
        KeyResponse(
            key = key,
            segCategory = segCategory,
            source = source,
            sourceId = sourceId,
            position = position,
            selected = selected,
        )
}
