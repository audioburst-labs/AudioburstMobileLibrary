
import com.audioburst.library.AudioburstLibrary
import com.audioburst.library.models.value
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val audioburstLibrary = AudioburstLibrary(applicationKey = "AndroidApp")
        val playlists = audioburstLibrary.getPlaylists()
        println(playlists)
        val playlist = audioburstLibrary.getPlaylist(playlists.value!!.first())
        println(playlist)
        val burstWithAd = playlist.value!!.bursts.firstOrNull { it.isAdAvailable }
        if (burstWithAd != null) {
            val adData = audioburstLibrary.getAdUrl(burstWithAd)
            println(adData)
        } else {
            println("None of Bursts has ad")
        }
    }
}