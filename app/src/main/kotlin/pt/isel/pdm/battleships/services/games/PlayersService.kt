package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.models.players.deployFleet.DeployFleetInput
import pt.isel.pdm.battleships.services.games.models.players.deployFleet.DeployFleetOutput
import pt.isel.pdm.battleships.services.games.models.players.fireShots.FireShotsInput
import pt.isel.pdm.battleships.services.games.models.players.fireShots.FireShotsOutput
import pt.isel.pdm.battleships.services.games.models.players.getMyFleet.GetMyFleetOutput
import pt.isel.pdm.battleships.services.games.models.players.getMyShots.GetMyShotsOutput
import pt.isel.pdm.battleships.services.games.models.players.getOpponentFleet.GetOpponentFleetOutput
import pt.isel.pdm.battleships.services.games.models.players.getOpponentShots.GetOpponentShotsOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * The service that handles the battleships game.
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
     * Gets my fleet.
     *
     * @param token the token of the user
     * @param getMyFleetLink the link to the get my fleet endpoint
     *
     * @return the API result of the get my fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMyFleet(
        token: String,
        getMyFleetLink: String
    ): APIResult<GetMyFleetOutput> =
        get(link = getMyFleetLink, token = token)

    /**
     * Deploys the fleet.
     *
     * @param token the token of the user
     * @param deployFleetLink the link to the deploy fleet endpoint
     * @param fleet the fleet to deploy
     *
     * @return the API result of the deploy fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun deployFleet(
        token: String,
        deployFleetLink: String,
        fleet: DeployFleetInput
    ): APIResult<DeployFleetOutput> =
        post(link = deployFleetLink, token = token, body = fleet)

    /**
     * Gets the opponent fleet.
     *
     * @param token the token of the user
     * @param getOpponentFleetLink the link to the get opponent fleet endpoint
     *
     * @return the API result of the get opponent fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentFleet(
        token: String,
        getOpponentFleetLink: String
    ): APIResult<GetOpponentFleetOutput> =
        get(link = getOpponentFleetLink, token = token)

    /**
     * Gets my shots.
     *
     * @param token the token of the user
     * @param getMyShotsLink the link to the get my shots endpoint
     *
     * @return the API result of the get my shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMyShots(
        token: String,
        getMyShotsLink: String
    ): APIResult<GetMyShotsOutput> =
        get(link = getMyShotsLink, token = token)

    /**
     * Fires a list of shots.
     *
     * @param token the token of the user
     * @param fireShotsLink the link to the fire shots endpoint
     * @param shots the shots to fire
     *
     * @return the API result of the fire shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun fireShots(
        token: String,
        fireShotsLink: String,
        shots: FireShotsInput
    ): APIResult<FireShotsOutput> =
        post(link = fireShotsLink, token = token, body = shots)

    /**
     * Gets the opponent shots.
     *
     * @param token the token of the user
     * @param getOpponentShotsLink the link to the get opponent shots endpoint
     *
     * @return the API result of the get opponent shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentShots(
        token: String,
        getOpponentShotsLink: String
    ): APIResult<GetOpponentShotsOutput> =
        get(link = getOpponentShotsLink, token = token)
}
