package com.audioburst.library.data.remote

import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*

internal data class Endpoint(
    val baseUrl: String,
    val protocol: Protocol,
    val path: String,
    val method: Method,
    val body: Body? = null,
    val queryParams: Map<String, Any?> = emptyMap(),
) {

    enum class Method {
        GET, POST,
    }

    enum class Protocol {
        Http, Https
    }

    sealed class Body {
        data class Json(val body: Any): Body()
        data class Plain(val text: String): Body()
        class Bytes(val byteArray: ByteArray): Body()
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
        this@toHttpRequest.body?.let {
            body = when (it) {
                is Endpoint.Body.Json -> {
                    headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
                    it.body
                }
                is Endpoint.Body.Plain -> TextContent(
                    text = it.text,
                    contentType = ContentType.Text.Plain
                )
                is Endpoint.Body.Bytes -> ByteArrayContent(bytes = it.byteArray)
            }
        }
    }
