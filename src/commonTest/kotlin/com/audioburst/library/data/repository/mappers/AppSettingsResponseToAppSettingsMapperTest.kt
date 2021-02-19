package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AppSettingsResponse
import com.audioburst.library.data.repository.models.PreferenceImageResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class AppSettingsResponseToAppSettingsMapperTest {

    private val mapper = AppSettingsResponseToAppSettingsMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val response = appSettingsResponseOf(
            preferences = (0..3).map {
                preferenceImageResponseOf(
                    name = "name$it",
                    image = "image$it"
                )
            }
        )

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(mapped.preferenceImages.size, response.preferences.size)
        mapped.preferenceImages.forEachIndexed { index, preferenceImage ->
            val responsePreference = response.preferences[index]
            assertEquals(preferenceImage.name, responsePreference.name)
            assertEquals(preferenceImage.imageUrl.value, responsePreference.image)
        }
    }
}

internal fun appSettingsResponseOf(
    preferences: List<PreferenceImageResponse> = emptyList()
): AppSettingsResponse =
    AppSettingsResponse(preferences = preferences)

internal fun preferenceImageResponseOf(
    name: String = "",
    image: String = "",
): PreferenceImageResponse =
    PreferenceImageResponse(
        name = name,
        image = image,
    )