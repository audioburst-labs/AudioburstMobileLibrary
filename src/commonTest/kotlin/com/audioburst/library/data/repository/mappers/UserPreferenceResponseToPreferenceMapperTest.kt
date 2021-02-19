package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.KeyResponse
import com.audioburst.library.data.repository.models.PreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.data.repository.preferenceImageOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        val mapped = mapper.map(response, emptyList())

        // THEN
        assertEquals(mapped.id, response.id)
        assertEquals(mapped.userId, response.userId)
        assertEquals(mapped.location, response.location)
        assertEquals(mapped.sourceType, response.sourceType)
        assertEquals(innerListsSize, response.preferences.size)
        assertTrue(mapped.preferences.all { it.iconUrl == null })
        mapped.preferences.forEach {
            assertEquals(it.keys.size, innerListsSize)
        }
    }

    @Test
    fun testIfImagePreferencesAreConnectedToPreferencesCorrectly() {
        // GIVEN
        val numberOfFirstPreferencesThatHasImage = 2
        val names = (0..5).map { "name$it" }
        val preferenceImages = names.mapIndexed { index, name ->
            preferenceImageOf(
                name = name,
                imageUrl = "image$index"
            )
        }.take(numberOfFirstPreferencesThatHasImage)

        val response = userPreferenceResponseOf(
            preferences = names.map {
                preferenceResponseOf(name = it)
            }
        )

        // WHEN
        val mapped = mapper.map(response, preferenceImages)

        // THEN
        assertEquals(numberOfFirstPreferencesThatHasImage, mapped.preferences.filter { it.iconUrl != null }.size)
        assertTrue(mapped.preferences.take(numberOfFirstPreferencesThatHasImage).all { it.iconUrl != null })
        assertTrue(mapped.preferences.drop(numberOfFirstPreferencesThatHasImage).all { it.iconUrl == null })
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
