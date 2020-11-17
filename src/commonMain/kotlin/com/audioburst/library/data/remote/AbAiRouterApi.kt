package com.audioburst.library.data.remote

import com.audioburst.library.data.repository.models.EventRequest

internal class AbAiRouterApi {

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

    private fun eventEndpoint(body: Endpoint.Body, name: String): Endpoint =
        endpoint(
            path = "event",
            method = Endpoint.Method.POST,
            queryParams = mapOf(
                "logLevel" to EVENT_LOG_LEVEL,
                "name" to name,
            ),
            body = body,
        )

    fun postEvent(eventRequest: EventRequest, name: String): Endpoint =
        eventEndpoint(
            body = Endpoint.Body.Json(eventRequest),
            name = name
        )

    companion object {
        private const val BASE_URL = "ab-ai-router.azurewebsites.net"

        private const val EVENT_LOG_LEVEL = "event"
    }
}
