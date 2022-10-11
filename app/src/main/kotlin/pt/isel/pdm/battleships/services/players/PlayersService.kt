package pt.isel.pdm.battleships.services.players

import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class PlayersService(
    private val apiEndpoint: String,
    private val httpClient: OkHttpClient,
    private val jsonFormatter: Gson
) {

    suspend fun getPlayerFleet(id: Int) {
        // TODO
    }

    suspend fun deployFleet(id: Int) {
        // TODO
    }

    suspend fun getOpponentFleet(id: Int) {
        // TODO
    }

    suspend fun getPlayerShots(id: Int) {
        // TODO
    }

    suspend fun createShots(id: Int) {
        // TODO
    }

    suspend fun getOpponentShots(id: Int) {
        // TODO
    }
}
