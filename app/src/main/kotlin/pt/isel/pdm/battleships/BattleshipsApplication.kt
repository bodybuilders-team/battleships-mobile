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
 * @property jsonEncoder the JSON formatter used by the application
 * @property sessionManager the session manager used by the application
 * @property battleshipsService the service used to handle the battleships game
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val jsonEncoder: Gson = GsonBuilder()
        .registerTypeAdapter(SubEntity::class.java, SubEntityDeserializer())
        .create()

    override val sessionManager: SessionManager = SessionManager()

    override val battleshipsService =
        BattleshipsService(
            apiEndpoint = API_ENDPOINT,
            httpClient = OkHttpClient(),
            jsonEncoder = jsonEncoder
        )

    companion object {
        const val API_ENDPOINT = "https://483b-2001-818-e871-b700-20b8-4cab-5c51-22bb.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
