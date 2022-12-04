package pt.isel.pdm.battleships

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.utils.siren.SubEntity
import pt.isel.pdm.battleships.services.utils.siren.SubEntityDeserializer

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

    override val sessionManager: SessionManager = SessionManager()

    override val battleshipsService = BattleshipsService(
        apiEndpoint = API_ENDPOINT,
        httpClient = OkHttpClient(),
        jsonEncoder = jsonEncoder
    )

    companion object {
        private const val API_ENDPOINT =
            "https://9543-2001-818-e871-b700-859a-59b-1bef-82df.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
