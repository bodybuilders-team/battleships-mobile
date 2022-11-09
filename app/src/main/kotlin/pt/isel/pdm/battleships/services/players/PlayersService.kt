package pt.isel.pdm.battleships.services.players

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.players.dtos.DeployFleetResponseDTO
import pt.isel.pdm.battleships.services.players.dtos.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class PlayersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) : HTTPService(apiEndpoint, httpClient, jsonFormatter) {

    suspend fun getPlayerFleet(id: Int) {
        // TODO
    }

    suspend fun deployFleet(
        token: String,
        deployLink: String,
        fleet: UndeployedFleetDTO
    ): HTTPResult<DeployFleetResponseDTO> =
        post(token, deployLink, fleet)

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
