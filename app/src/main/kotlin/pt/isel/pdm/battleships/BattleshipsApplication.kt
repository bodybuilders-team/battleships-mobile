package pt.isel.pdm.battleships

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.BattleshipsService

/**
 * Represents the Battleships application.
 *
 * @property jsonFormatter the JSON formatter used by the application
 * @property sessionManager the session manager used by the application
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val jsonFormatter: Gson = Gson()
    override val sessionManager: SessionManager = SessionManager()

    override val battleshipsService =
        BattleshipsService(
            apiEndpoint = API_ENDPOINT,
            httpClient = OkHttpClient(),
            jsonFormatter = jsonFormatter
        )

    companion object {
        const val API_ENDPOINT = "https://0d3d-2001-8a0-6370-f300-10a3-5c76-dc6d-c308.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
