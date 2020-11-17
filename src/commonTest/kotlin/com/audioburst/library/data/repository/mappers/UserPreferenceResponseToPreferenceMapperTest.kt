package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.KeyResponse
import com.audioburst.library.data.repository.models.PreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferenceResponseToPreferenceMapperTest {

    private val mapper = UserPreferenceResponseToPreferenceMapper()

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
        val mapped = mapper.map(response)

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

internal fun userPreferenceResponseOf(
    id: String = "",
    userId: String = "",
    location: String? = null,
    sourceType: String = "",
    preferences: List<PreferenceResponse> = listOf(),
): UserPreferenceResponse =
    UserPreferenceResponse(
        id = id,
        userId = userId,
        location = location,
        sourceType = sourceType,
        preferences = preferences,
    )

internal fun preferenceResponseOf(
    name: String = "",
    source: String = "",
    take: Int = 0,
    offer: Int = 0,
    keys: List<KeyResponse> = listOf(),
): PreferenceResponse =
    PreferenceResponse(
        name = name,
        source = source,
        take = take,
        offer = offer,
        keys = keys,
    )

internal fun keyResponseOf(
    key: String = "",
    segCategory: String = "",
    source: String = "",
    sourceId: Int = 0,
    position: Int = 0,
    selected: Boolean = false,
): KeyResponse =
    KeyResponse(
        key = key,
        segCategory = segCategory,
        source = source,
        sourceId = sourceId,
        position = position,
        selected = selected,
    )
