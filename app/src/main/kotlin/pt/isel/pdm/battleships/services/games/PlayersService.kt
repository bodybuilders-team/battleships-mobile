package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.dtos.ship.DeployFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON formatter
 */
class PlayersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getPlayerFleet(id: Int) {
        // TODO
    }

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun deployFleet(
        token: String,
        deployLink: String,
        fleet: UndeployedFleetDTO
    ): APIResult<DeployFleetResponseDTO> =
        post(deployLink, token, fleet)

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentFleet(id: Int) {
        // TODO
    }

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getPlayerShots(id: Int) {
        // TODO
    }

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createShots(id: Int) {
        // TODO
    }

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentShots(id: Int) {
        // TODO
    }
}
