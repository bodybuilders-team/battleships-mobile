package pt.isel.pdm.battleships.service.services.games

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.connection.APIResult
import pt.isel.pdm.battleships.service.connection.UnexpectedResponseException
import pt.isel.pdm.battleships.service.services.games.models.players.deployFleet.DeployFleetInput
import pt.isel.pdm.battleships.service.services.games.models.players.deployFleet.DeployFleetOutput
import pt.isel.pdm.battleships.service.services.games.models.players.fireShots.FireShotsInput
import pt.isel.pdm.battleships.service.services.games.models.players.fireShots.FireShotsOutput
import pt.isel.pdm.battleships.service.services.games.models.players.getMyFleet.GetMyFleetOutput
import pt.isel.pdm.battleships.service.services.games.models.players.getMyShots.GetMyShotsOutput
import pt.isel.pdm.battleships.service.services.games.models.players.getOpponentFleet.GetOpponentFleetOutput
import pt.isel.pdm.battleships.service.services.games.models.players.getOpponentShots.GetOpponentShotsOutput
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels
import java.io.IOException

/**
 * The service that handles the players, and keeps track of the links to the endpoints.
 *
 * @property sessionManager the manager used to handle the user session
 * @property links the links to the endpoints
 * @property playersService the service that handles the players
 */
class LinkPlayersService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val playersService: PlayersService
) {
    private val token
        get() = sessionManager.accessToken ?: throw IllegalStateException("No token available")

    /**
     * Gets my fleet.
     *
     * @return the API result of the get my fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMyFleet(): APIResult<GetMyFleetOutput> =
        playersService.getMyFleet(
            token = token,
            getMyFleetLink = links[Rels.GET_MY_FLEET]
                ?: throw IllegalArgumentException("The get my fleet link is missing")
        )

    /**
     * Deploys the fleet.
     *
     * @param fleet the fleet to deploy
     *
     * @return the API result of the deploy fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun deployFleet(fleet: DeployFleetInput): APIResult<DeployFleetOutput> =
        playersService.deployFleet(
            token = token,
            deployFleetLink = links[Rels.DEPLOY_FLEET]
                ?: throw IllegalArgumentException("The deploy fleet link is missing"),
            fleet = fleet
        )

    /**
     * Gets the opponent fleet.
     *
     * @return the API result of the get opponent fleet request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentFleet(): APIResult<GetOpponentFleetOutput> =
        playersService.getOpponentFleet(
            token = token,
            getOpponentFleetLink = links[Rels.GET_OPPONENT_FLEET]
                ?: throw IllegalArgumentException("The get opponent fleet link is missing")
        )

    /**
     * Gets my shots.
     *
     * @return the API result of the get my shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMyShots(): APIResult<GetMyShotsOutput> =
        playersService.getMyShots(
            token = token,
            getMyShotsLink = links[Rels.GET_MY_SHOTS]
                ?: throw IllegalArgumentException("The get my shots link is missing")
        )

    /**
     * Fires a list of shots.
     *
     * @param shots the shots to fire
     *
     * @return the API result of the fire shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun fireShots(shots: FireShotsInput): APIResult<FireShotsOutput> =
        playersService.fireShots(
            token = token,
            fireShotsLink = links[Rels.FIRE_SHOTS]
                ?: throw IllegalArgumentException("The fire shots link is missing"),
            shots = shots
        )

    /**
     * Gets the opponent shots.
     *
     * @return the API result of the get opponent shots request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentShots(): APIResult<GetOpponentShotsOutput> =
        playersService.getOpponentShots(
            token = token,
            getOpponentShotsLink = links[Rels.GET_OPPONENT_SHOTS]
                ?: throw IllegalArgumentException("The get opponent shots link is missing")
        )
}
