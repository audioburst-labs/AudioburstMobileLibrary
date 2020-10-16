
import com.audioburst.library.AudioburstLibrary
import com.audioburst.library.data.result
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val audioburstLibrary = AudioburstLibrary(applicationKey = "AndroidApp")
        val playlists = audioburstLibrary.getPlaylists()
        println(playlists)
        val playlist = audioburstLibrary.getPlaylist(playlists.result()!!.first())
        println(playlist)
        val burstWithAd = playlist.result()!!.bursts.firstOrNull { it.isAdAvailable }
        if (burstWithAd != null) {
            val adData = audioburstLibrary.getAdData(burstWithAd)
            println(adData)
        } else {
            println("None of Bursts has ad")
        }
    }
}