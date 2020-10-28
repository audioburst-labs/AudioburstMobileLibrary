package com.audioburst.library.data.remote

import com.audioburst.library.models.LibraryKey
import com.audioburst.library.models.SubscriptionKey

internal class AudioburstApi {

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

    fun getBurstPlay(libraryKey: LibraryKey, playlistId: Long, burstId: String, userId: String, subscriptionKey: SubscriptionKey, downloadType: Int): Endpoint =
        endpoint(
            path = "audio/repo/play/${libraryKey.value}/$playlistId/$burstId.mp3",
            method = Endpoint.Method.POST,
            queryParams = mapOf(
                "userId" to userId,
                "appName" to subscriptionKey.value,
                "downloadType" to downloadType
            )
        )

    companion object {
        private const val BASE_URL = "sapi.audioburst.com"
    }
}
