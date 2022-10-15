package pt.isel.pdm.battleships

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.utils.siren.SubEntity
import pt.isel.pdm.battleships.services.utils.siren.SubEntityDeserializer

/**
 * Represents the Battleships application.
 *
 * @property jsonFormatter the JSON formatter used by the application
 * @property sessionManager the session manager used by the application
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val jsonFormatter: Gson = GsonBuilder()
        .registerTypeAdapter(SubEntity::class.java, SubEntityDeserializer())
        .create()

    override val sessionManager: SessionManager = SessionManager()

    override val battleshipsService =
        BattleshipsService(
            apiEndpoint = API_ENDPOINT,
            httpClient = OkHttpClient(),
            jsonFormatter = jsonFormatter
        )

    companion object {
        const val API_ENDPOINT = "https://68a1-95-92-71-62.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
