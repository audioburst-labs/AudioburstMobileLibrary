package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetPlaylistTest {

    private fun interactor(
        getPlaylistReturn: Resource<Playlist>,
        userResource: Resource<User>,
    ): GetPlaylist =
        GetPlaylist(
            getUser = getUserOf(userResource),
            userRepository = userRepositoryOf(returns = MockUserRepository.Returns(getPlaylist = getPlaylistReturn)),
        )

    @Test
    fun testIfResourceDataIsReturnedWhenRepositoryReturnsThat() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistReturn = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Data)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = resourceErrorOf()

        // WHEN
        val resource = interactor(
            getPlaylistReturn = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfGetUserReturnsUserAndUserRepositoryReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylistReturn = resourceErrorOf()
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistReturn = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Error)
    }
}
