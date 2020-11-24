package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.RegisterResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterResponseToUserTest {

    private val mapper = RegisterResponseToUserMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val response = registerResponseOf()

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(mapped.userId, response.ABUserID)
    }
}

internal fun registerResponseOf(
    ABUserID: String = "",
    AppName: String = "",
    EntryDate: String = "",
    IP: String = "",
    ExternalUserId: String = "",
) : RegisterResponse =
    RegisterResponse(
        ABUserID = ABUserID,
        AppName = AppName,
        EntryDate = EntryDate,
        IP = IP,
        ExternalUserId = ExternalUserId,
    )
