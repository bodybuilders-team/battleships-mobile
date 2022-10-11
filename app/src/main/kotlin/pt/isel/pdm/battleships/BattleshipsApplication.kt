package pt.isel.pdm.battleships

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.service.BattleshipsService

/**
 * Represents the Battleships application.
 *
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val jsonFormatter: Gson = Gson()

    override val sessionManager: SessionManager = SessionManager()

    override val battleshipsService =
        BattleshipsService(
            API_ENDPOINT,
            OkHttpClient(),
            jsonFormatter
        )

    companion object {
        const val API_ENDPOINT = "https://6ca6-2001-8a0-6370-f300-18f7-d26d-1d5d-d612.eu.ngrok.io"
        const val TAG = "BattleshipsApp"
    }
}
