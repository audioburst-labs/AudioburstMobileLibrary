
import com.audioburst.library.data.Resource
import com.audioburst.library.di.Injector
import com.audioburst.library.models.SubscriptionKey
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        Injector.provideSubscriptionKeySetter().set(SubscriptionKey("AndroidApp"))
        val userRepository = Injector.provideUserRepository()
        val userId = (userRepository.registerUser("userId") as Resource.Data).result.userId
        println(userId)
        val playlists = userRepository.getPlaylists(userId)
        println(playlists)
        val playlist = userRepository.getPlaylist(userId, (userRepository.getPlaylists(userId) as Resource.Data).result.first())
        println(playlist)
    }
}