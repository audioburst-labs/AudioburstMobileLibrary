package com.audioburst.library.data.remote

internal class AudioburstV2Api {

    private fun endpoint(
        path: String,
        method: Endpoint.Method,
        body: Any? = null,
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

    companion object {
        private const val BASE_URL = "sapi.audioburst.com/v2"
    }
}
