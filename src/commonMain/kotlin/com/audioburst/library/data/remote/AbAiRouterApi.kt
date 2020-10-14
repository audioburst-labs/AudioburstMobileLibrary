package com.audioburst.library.data.remote

import com.audioburst.library.data.repository.models.AdvertisementEventRequest
import com.audioburst.library.data.repository.models.EventRequest

internal class AbAiRouterApi {

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

    private fun eventEndpoint(body: Any, name: String): Endpoint =
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
            body = eventRequest,
            name = name
        )

    fun postEvent(advertisementEventRequest: AdvertisementEventRequest): Endpoint =
        eventEndpoint(
            body = advertisementEventRequest,
            name = AD_EVENT_NAME
        )

    companion object {
        private const val BASE_URL = "ab-ai-router.azurewebsites.net"

        private const val EVENT_LOG_LEVEL = "event"

        private const val AD_EVENT_NAME = "AD"
    }
}
