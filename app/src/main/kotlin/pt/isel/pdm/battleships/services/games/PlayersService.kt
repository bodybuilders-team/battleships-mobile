package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import java.io.IOException
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.dtos.ship.DeployFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.GetMyFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.GetOpponentFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.GetOpponentShotsDTO
import pt.isel.pdm.battleships.services.utils.APIResult

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
    suspend fun getMyFleet(
        token: String,
        getMyFleetLink: String
    ): APIResult<GetMyFleetResponseDTO> =
        get(link = getMyFleetLink, token = token)

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
        post(link = deployLink, token = token, body = fleet)

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentFleet(
        token: String,
        getOpponentFleetLink: String
    ): APIResult<GetOpponentFleetResponseDTO> =
        get(link = getOpponentFleetLink, token = token)

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getPlayerShots(id: Int) {
        // TODO: To be implemented
    }

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun fireShots(
        token: String,
        fireShotsLink: String,
        fireShotsDTO: FireShotsDTO
    ): APIResult<FireShotsResponseDTO> =
        post(link = fireShotsLink, token = token, body = fireShotsDTO)

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentShots(
        token: String,
        getOpponentShotsLink: String
    ): APIResult<GetOpponentShotsDTO> =
        get(link = getOpponentShotsLink, token = token)
}
