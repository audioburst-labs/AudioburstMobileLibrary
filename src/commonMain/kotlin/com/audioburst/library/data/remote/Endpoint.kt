package com.audioburst.library.data.remote

import io.ktor.client.request.*
import io.ktor.http.*

internal data class Endpoint(
    val baseUrl: String,
    val protocol: Protocol,
    val path: String,
    val method: Method,
    val body: Any? = null,
    val queryParams: Map<String, Any?> = emptyMap(),
) {

    enum class Method {
        GET, POST,
    }

    enum class Protocol {
        Http, Https
    }
}

internal inline fun Endpoint.toHttpRequest(): HttpRequestBuilder =
    HttpRequestBuilder {
        protocol = when (this@toHttpRequest.protocol) {
            Endpoint.Protocol.Http -> URLProtocol.HTTP
            Endpoint.Protocol.Https -> URLProtocol.HTTPS
        }
        host = baseUrl
        encodedPath = this@toHttpRequest.path
    }.apply {
        method = when (this@toHttpRequest.method) {
            Endpoint.Method.GET -> HttpMethod.Get
            Endpoint.Method.POST -> HttpMethod.Post
        }
        queryParams.forEach { parameter(it.key, it.value) }
        // Temporary workaround to the bug where Content-Length is not added to the request when body is empty
        // https://youtrack.jetbrains.com/issue/KTOR-556
        body = this@toHttpRequest.body ?: ""
    }
