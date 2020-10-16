package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetPlaylistsInfoTest {

    private fun interactor(
        getPlaylists: Resource<List<PlaylistInfo>>,
        userResource: Resource<User>,
    ): GetPlaylistsInfo =
        GetPlaylistsInfo(
            getUser = getUserOf(userResource),
            userRepository = userRepositoryOf(returns = MockUserRepository.Returns(getPlaylists = getPlaylists)),
        )

    @Test
    fun testIfResourceDataIsReturnedWhenRepositoryReturnsThat() = runTest {
        // GIVEN
        val getPlaylists = Resource.Data(listOf(playlistInfoOf()))
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylists = getPlaylists,
            userResource = userResource,
        )()

        // THEN
        assertTrue(resource is Resource.Data)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylists = Resource.Data(listOf(playlistInfoOf()))
        val userResource = resourceErrorOf()

        // WHEN
        val resource = interactor(
            getPlaylists = getPlaylists,
            userResource = userResource,
        )()

        // THEN
        assertTrue(resource is Resource.Error)
    }

    @Test
    fun testIfGetUserReturnsUserAndUserRepositoryReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylists = resourceErrorOf()
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylists = getPlaylists,
            userResource = userResource,
        )()

        // THEN
        assertTrue(resource is Resource.Error)
    }
}
