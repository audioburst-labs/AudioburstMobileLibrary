package com.audioburst.library.data.remote

internal class AudioburstStorageApi {

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

    fun getMobileSettings(): Endpoint =
        endpoint(
            path = "api/mobileSettings_V01.json",
            method = Endpoint.Method.GET,
        )

    companion object {
        private const val BASE_URL = "storageaudiobursts.blob.core.windows.net"
    }
}