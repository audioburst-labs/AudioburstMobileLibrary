package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.Key
import com.audioburst.library.models.Preference
import com.audioburst.library.models.UserPreferences
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PreferenceToUserPreferenceResponseMapperTest {

    private val mapper = PreferenceToUserPreferenceResponseMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val innerListsSize = 5
        val response = userPreferenceResponseOf(
            preferences = (0 until innerListsSize).map {
                preferenceResponseOf(
                    name = it.toString(),
                    keys = (0 until innerListsSize).map {
                        keyResponseOf(
                            key = it.toString()
                        )
                    }
                )
            }
        )

        // WHEN
        val mapped = mapper.map(userPreferenceOf())

        // THEN
        assertEquals(response.id, mapped.id)
        assertEquals(response.userId, mapped.userId)
        assertEquals(response.location, mapped.location)
        assertEquals(response.sourceType, mapped.sourceType)
        assertEquals(response.preferences.size, innerListsSize)
        response.preferences.forEach {
            assertEquals(it.keys.size, innerListsSize)
        }
    }
}

internal fun userPreferenceOf(
    id: String = "",
    userId: String = "",
    location: String? = null,
    sourceType: String = "",
    preferences: List<Preference> = listOf(),
): UserPreferences =
    UserPreferences(
        id = id,
        userId = userId,
        location = location,
        sourceType = sourceType,
        preferences = preferences,
    )

internal fun preferenceOf(
    name: String = "",
    source: String = "",
    take: Int = 0,
    offer: Int = 0,
    keys: List<Key> = listOf(),
): Preference =
    Preference(
        name = name,
        source = source,
        take = take,
        offer = offer,
        keys = keys,
    )

internal fun keyOf(
    key: String = "",
    segCategory: String = "",
    source: String = "",
    sourceId: Int = 0,
    position: Int = 0,
    selected: Boolean = false,
): Key =
    Key(
        key = key,
        segCategory = segCategory,
        source = source,
        sourceId = sourceId,
        position = position,
        selected = selected,
    )

