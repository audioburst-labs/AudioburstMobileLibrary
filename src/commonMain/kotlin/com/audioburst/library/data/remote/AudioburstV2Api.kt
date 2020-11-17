package com.audioburst.library.data.remote

import com.audioburst.library.data.repository.models.UserPreferenceResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class AudioburstV2Api(private val json: Json) {

    private fun endpoint(
        path: String,
        method: Endpoint.Method,
        body: Endpoint.Body? = null,
        queryParams: Map<String, Any?> = emptyMap()
    ): Endpoint =
        Endpoint(
            baseUrl = BASE_URL,
            protocol = Endpoint.Protocol.Https,
            path = path,
            method = method,
            body = body,
            queryParams = queryParams
        )

    fun registerUser(userId: String): Endpoint =
        endpoint(
            path = "users/register",
            method = Endpoint.Method.POST,
            queryParams = mapOf(
                "externalUserId" to userId
            )
        )

    fun getAllPlaylists(): Endpoint =
        endpoint(
            path = "playlists",
            method = Endpoint.Method.GET,
        )

    fun getUserPreferences(userId: String): Endpoint =
        endpoint(
            path = "user/preferences",
            method = Endpoint.Method.GET,
            queryParams = mapOf(
                "userId" to userId
            )
        )

    fun setUserPreferences(userId: String, userPreferences: UserPreferenceResponse): Endpoint =
        endpoint(
            path = "user/preferences",
            method = Endpoint.Method.POST,
            queryParams = mapOf(
                "userId" to userId
            ),
            body = Endpoint.Body.Plain(json.encodeToString(userPreferences))
        )

    companion object {
        private const val BASE_URL = "sapi.audioburst.com/v2"
    }
}
