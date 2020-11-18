
import com.audioburst.library.AudioburstLibrary
import com.audioburst.library.models.Key
import com.audioburst.library.models.onData
import com.audioburst.library.models.onError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val audioburstLibrary = AudioburstLibrary(applicationKey = "AndroidApp")

        audioburstLibrary.getUserPreferences().onData { userPreferences ->
            audioburstLibrary.setUserPreferences(
                userPreferences.copy(
                    preferences = userPreferences.preferences.map { preferences ->
                        preferences.copy(
                            keys = preferences.keys.mapIndexed { index: Int, key: Key ->
                                if (index == 0) {
                                    key.copy(selected = true)
                                } else {
                                    key
                                }
                            }
                        )
                    }
                )
            )
        }
        audioburstLibrary
            .getPersonalPlaylist()
            .collect { result ->
                result
                    .onData { pendingPlaylist ->
                        println("isReady: ${pendingPlaylist.isReady}, ids: ${pendingPlaylist.playlist.bursts.joinToString { it.id }}")
                    }
                    .onError { println("getPlaylists: ${it.message}") }
            }

        audioburstLibrary.getPlaylists()
            .onData { playlists ->
                playlists.forEach { playlistInfo ->
                    audioburstLibrary.getPlaylist(playlistInfo)
                        .onData { playlist ->
                            val burstWithId = playlist.bursts.firstOrNull { it.isAdAvailable }
                            if (burstWithId != null) {
                                audioburstLibrary.getAdUrl(burstWithId)
                                    .onData { println("adUrl: $it") }
                                    .onError { println("getAdUrl: ${it.message}") }
                            } else {
                                println("None bursts has ad")
                            }
                        }
                        .onError { println("getPlaylist: ${it.message}") }
                }
            }
            .onError { println("getPlaylists: ${it.message}") }
    }
}