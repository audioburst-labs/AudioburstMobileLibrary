package com.audioburst.library.data

import com.audioburst.library.interactors.resourceErrorOf
import com.audioburst.library.models.Result
import kotlin.test.Test
import kotlin.test.assertTrue

class ResourceTest {

    @Test
    fun testIfResourceDataIsGettingMappedToResultData() {
        // GIVEN
        val resource = Resource.Data(Unit)

        // WHEN
        val result = resource.asResult()

        // THEN
        assertTrue(result is Result.Data)
    }

    @Test
    fun testIfResourceErrorIsGettingMappedToResultError() {
        // GIVEN
        val resource = resourceErrorOf()

        // WHEN
        val result = resource.asResult()

        // THEN
        assertTrue(result is Result.Error)
    }
}