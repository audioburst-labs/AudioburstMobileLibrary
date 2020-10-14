package com.audioburst.library.data.remote

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EndpointTest {

    private val baseUrl = "sapi.audioburst.com"
    private fun sapiEndpoint(
        protocol: Endpoint.Protocol = Endpoint.Protocol.Https,
        path: String = "",
        method: Endpoint.Method = Endpoint.Method.GET,
        body: Any? = null,
        queryParams: Map<String, Any?> = emptyMap(),
    ) = endpointOf(
        baseUrl = baseUrl,
        protocol = protocol,
        path = path,
        method = method,
        body = body,
        queryParams = queryParams,
    )

    @Test
    fun testIfEndpointIsCorrectlyTransformedIntoUrl() {
        // GIVEN
        val path = "users"
        val method = Endpoint.Method.GET
        val protocol = Endpoint.Protocol.Https
        val body = "body"
        val endpoint = sapiEndpoint(
            path = path,
            method = method,
            protocol = protocol,
            body = body,
        )

        // WHEN
        val request = endpoint.toHttpRequest()
        val url = request.url.buildString()

        // THEN
        assertEquals(request.method.value, method.name)
        assertEquals(request.body, body)
        assertTrue(url.contains(path))
        assertTrue(url.contains(protocol.name, ignoreCase = true))
        assertTrue(url.contains(baseUrl))
        assertTrue(!url.contains("?"))
    }

    @Test
    fun testIfCreatedUrlContainsAllQueryParams() {
        // GIVEN
        val path = "users"
        val queryParams: Map<String, Any?> = mapOf(
            "queryName1" to "queryValue1",
            "queryName2" to "queryValue2",
            "queryName3" to "queryValue3",
        )
        val endpoint = sapiEndpoint(
            path = path,
            queryParams = queryParams,
        )

        // WHEN
        val url = endpoint.toHttpRequest().url.buildString()

        // THEN
        queryParams.forEach {
            assertTrue(url.contains("${it.key}=${it.value}"))
        }
        assertTrue(url.contains("?"))
        assertEquals(url.count { it == '&' }, queryParams.size-1)
    }
}

internal fun endpointOf(
    baseUrl: String = "",
    protocol: Endpoint.Protocol = Endpoint.Protocol.Https,
    path: String = "",
    method: Endpoint.Method = Endpoint.Method.GET,
    body: Any? = null,
    queryParams: Map<String, Any?> = emptyMap(),
): Endpoint =
    Endpoint(
        baseUrl = baseUrl,
        protocol = protocol,
        path = path,
        method = method,
        body = body,
        queryParams = queryParams,
    )