package com.audioburst.library.data.remote

import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.models.LibraryKey
import com.audioburst.library.models.PersonalPlaylistQueryId
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

    fun verify(userId: String): Endpoint =
        endpoint(
            path = "users/verify",
            method = Endpoint.Method.GET,
            queryParams = mapOf(
                "userId" to userId
            )
        )

    fun getAllPlaylists(): Endpoint =
        endpoint(
            path = "playlists",
            method = Endpoint.Method.GET,
            queryParams = mapOf(
                "withDisplay" to true
            )
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

    fun getPersonalPlaylistQueryId(userId: String): Endpoint =
        endpoint(
            path = "personal",
            method = Endpoint.Method.GET,
            queryParams = mapOf(
                "userId" to userId,
                "async" to true,
                "nocache" to false,
                "prefix" to false,
            ),
        )

    fun getPersonalPlaylist(personalPlaylistQueryId: PersonalPlaylistQueryId): Endpoint =
        endpoint(
            path = "personal/async/${personalPlaylistQueryId.value}?format=v2",
            method = Endpoint.Method.GET,
        )

    fun getPlaylist(byteArray: ByteArray, userId: String, libraryKey: LibraryKey): Endpoint =
        endpoint(
            path = "search/voice?userId=${userId}&appKey=${libraryKey.value}",
            method = Endpoint.Method.POST,
            body = Endpoint.Body.Bytes(byteArray),
        )

    fun search(query: String, userId: String): Endpoint =
        endpoint(
            path = "search/keyword",
            queryParams = mapOf(
                "q" to query,
                "userId" to userId,
            ),
            method = Endpoint.Method.GET,
        )

    fun getUserGeneratedPlaylist(id: String, userId: String, device: String): Endpoint =
        endpoint(
            path = "topstories/usergenerated/$id",
            queryParams = mapOf(
                "userId" to userId,
                "device" to device,
            ),
            method = Endpoint.Method.GET,
        )

    fun getSourcePlaylist(id: String, userId: String, device: String): Endpoint =
        endpoint(
            path = "topstories/source/$id",
            queryParams = mapOf(
                "userId" to userId,
                "device" to device,
            ),
            method = Endpoint.Method.GET,
        )

    fun getAccountPlaylist(id: String, userId: String, device: String): Endpoint =
        endpoint(
            path = "topstories/account/$id",
            queryParams = mapOf(
                "userId" to userId,
                "device" to device,
            ),
            method = Endpoint.Method.GET,
        )

    fun getChannelPlaylist(id: Int, userId: String, device: String): Endpoint =
        endpoint(
            path = "topstories/category",
            queryParams = mapOf(
                "userId" to userId,
                "device" to device,
                "category" to id,
            ),
            method = Endpoint.Method.GET,
        )

    fun getShareUrl(burstId: String, queryParams: Map<String, String>): Endpoint =
        endpoint(
            path = "share/burst/shortener/$burstId",
            queryParams = queryParams,
            method = Endpoint.Method.GET,
        )

    companion object {
        private const val BASE_URL = "sapi.audioburst.com/v2"
    }
}
