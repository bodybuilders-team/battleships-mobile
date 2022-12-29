package pt.isel.pdm.battleships

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.service.media.siren.SubEntity
import pt.isel.pdm.battleships.service.media.siren.SubEntityDeserializer
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.session.SessionManagerSharedPrefs

/**
 * The Battleships application.
 *
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property battleshipsService the service used to handle the battleships game
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val jsonEncoder: Gson = GsonBuilder()
        .registerTypeAdapter(SubEntity::class.java, SubEntityDeserializer())
        .create()

    override val sessionManager: SessionManager = SessionManagerSharedPrefs(context = this)

    override val battleshipsService = BattleshipsService(
        apiEndpoint = API_ENDPOINT,
        httpClient = OkHttpClient(),
        jsonEncoder = jsonEncoder
    )

    companion object {
        private const val API_ENDPOINT =
            " https://a409-2001-8a0-6370-f300-fcd0-2e04-8396-8ee5.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
