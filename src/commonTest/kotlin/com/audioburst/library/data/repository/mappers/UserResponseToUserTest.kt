package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.UserResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class UserResponseToUserTest {

    private val mapper = UserResponseToUserMapper()

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
) : UserResponse =
    UserResponse(
        ABUserID = ABUserID,
        AppName = AppName,
        EntryDate = EntryDate,
        IP = IP,
        ExternalUserId = ExternalUserId,
    )
