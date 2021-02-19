package com.audioburst.library.data

import com.audioburst.library.data.remote.endpointOf
import com.audioburst.library.models.Url
import com.audioburst.library.runTest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertTrue

class HttpClientTest {

    private fun httpClientThatRespondsWith(response: MockRequestHandler): HttpClient =
        HttpClient(MockEngine) {
            engine {
                addHandler(response)
            }
        }

    @Test
    fun testIfExecuteEndpointReturnsResourceDataWhenResponseIsOk() = runTest {
        // GIVEN
        val endpoint = endpointOf()

        // WHEN
        val resource = httpClientThatRespondsWith { respondOk() }.execute<Unit>(endpoint)

        // THEN
        assertTrue(resource is Resource.Data)
    }

    @Test
    fun testIfExecuteUrlReturnsResourceDataWhenResponseIsOk() = runTest {
        // GIVEN
        val url = Url("www.google.com")

        // WHEN
        val resource = httpClientThatRespondsWith { respondOk() }.execute<Unit>(url)

        // THEN
        assertTrue(resource is Resource.Data)
    }

    @Test
    fun testIfExecuteEndpointReturnsResourceErrorWhenResponseThrowsException() = runTest {
        // GIVEN
        val endpoint = endpointOf()

        // WHEN
        val resource = httpClientThatRespondsWith { throw Exception() }.execute<Unit>(endpoint)

        // THEN
        assertTrue(resource is Resource.Error)
    }

    @Test
    fun testIfExecuteUrlReturnsResourceErrorWhenResponseThrowsException() = runTest {
        // GIVEN
        val url = Url("www.google.com")

        // WHEN
        val resource = httpClientThatRespondsWith { throw Exception() }.execute<Unit>(url)

        // THEN
        assertTrue(resource is Resource.Error)
    }

    @Test
    fun testIfExecuteEndpointReturnsResourceErrorWhenResponseIsError() = runTest {
        // GIVEN
        val endpoint = endpointOf()

        // WHEN
        val resource = httpClientThatRespondsWith { respondBadRequest() }.execute<Unit>(endpoint)

        // THEN
        assertTrue(resource is Resource.Error)
    }

    @Test
    fun testIfExecuteUrlReturnsResourceErrorWhenResponseIsError() = runTest {
        // GIVEN
        val url = Url("www.google.com")

        // WHEN
        val resource = httpClientThatRespondsWith { respondBadRequest() }.execute<Unit>(url)

        // THEN
        assertTrue(resource is Resource.Error)
    }
}

internal inline fun <reified T> httpClientOf(resource: Resource<T>): HttpClient =
    HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler {
                when (resource) {
                    is Resource.Data -> {
                        val json = kotlinx.serialization.json.Json {  }.encodeToString(resource.result)
                        respond(json, HttpStatusCode.OK, headers = headersOf("Content-Type", ContentType.Application.Json.toString()))
                    }
                    is Resource.Error -> when (resource.errorType) {
                        is ErrorType.ConnectionError -> throw IOException("")
                        is ErrorType.UnexpectedException -> throw Exception()
                        ErrorType.HttpError.BadRequestException -> respondError(status = HttpStatusCode.BadRequest)
                        ErrorType.HttpError.UnauthorizedException -> respondError(status = HttpStatusCode.Unauthorized)
                        ErrorType.HttpError.ForbiddenException -> respondError(status = HttpStatusCode.Forbidden)
                        ErrorType.HttpError.NotFoundException -> respondError(status = HttpStatusCode.NotFound)
                        ErrorType.HttpError.InternalServerErrorException -> respondError(status = HttpStatusCode.InternalServerError)
                        is ErrorType.HttpError.UnexpectedException -> respondError(status = HttpStatusCode.MultipleChoices)
                    }
                }
            }
        }
    }
