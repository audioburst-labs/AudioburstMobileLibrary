package com.audioburst.library.data

import com.audioburst.library.data.remote.endpointOf
import com.audioburst.library.models.Url
import com.audioburst.library.runTest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
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
