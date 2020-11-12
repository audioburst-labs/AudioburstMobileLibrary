
import com.audioburst.library.AudioburstLibrary
import com.audioburst.library.models.onData
import com.audioburst.library.models.onError
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val audioburstLibrary = AudioburstLibrary(applicationKey = "AndroidApp")

        audioburstLibrary.getPlaylists()
            .onData { playlists ->
                playlists.forEach { playlistInfo ->
                    audioburstLibrary.getPlaylist(playlistInfo)
                        .onData { playlist ->
                            val burstWithId = playlist.bursts.firstOrNull { it.isAdAvailable }
                            if (burstWithId != null) {
                                audioburstLibrary.getAdUrl(burstWithId)
                                    .onData {
                                        println("adUrl: $it")
                                    }
                                    .onError {
                                        println("getAdUrl: ${it.message}")
                                    }
                            } else {
                                println("None bursts has ad")
                            }
                        }
                        .onError {
                            println("getPlaylist: ${it.message}")
                        }
                }
            }
            .onError {
                println("getPlaylists: ${it.message}")
            }
    }
}