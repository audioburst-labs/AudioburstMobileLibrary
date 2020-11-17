package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.KeyResponse
import com.audioburst.library.data.repository.models.PreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.models.Preference
import com.audioburst.library.models.UserPreferences
import com.audioburst.library.models.Key

internal class UserPreferenceResponseToPreferenceMapper {

    fun map(from: UserPreferenceResponse): UserPreferences =
        UserPreferences(
            id = from.id,
            userId = from.userId,
            location = from.location,
            sourceType = from.sourceType,
            preferences = from.preferences.map { it.toCategory() },
        )

    private fun PreferenceResponse.toCategory(): Preference =
        Preference(
            name = name,
            source = source,
            take = take,
            offer = offer,
            keys = keys.map { it.toTopic() },
        )

    private fun KeyResponse.toTopic(): Key =
        Key(
            key = key,
            segCategory = segCategory,
            source = source,
            sourceId = sourceId,
            position = position,
            selected = selected,
        )
}
