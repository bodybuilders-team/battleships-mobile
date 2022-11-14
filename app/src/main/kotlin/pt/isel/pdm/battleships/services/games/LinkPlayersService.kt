package pt.isel.pdm.battleships.services.games

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.dtos.ship.DeployFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.GetMyFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.GetOpponentFleetResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsResponseDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.GetOpponentShotsDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.io.IOException

/**
 * TODO Comment
 */
class LinkPlayersService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val playersService: PlayersService
) {
    private val token
        get() = sessionManager.accessToken
            ?: throw IllegalStateException("No token available")

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMyFleet(): APIResult<GetMyFleetResponseDTO> =
        playersService.getMyFleet(
            token = token,
            getMyFleetLink = links[Rels.GET_MY_FLEET]
                ?: throw IllegalArgumentException("The get my fleet link is missing")
        )

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun deployFleet(
        fleet: UndeployedFleetDTO
    ): APIResult<DeployFleetResponseDTO> =
        playersService.deployFleet(
            token = token,
            deployLink = links[Rels.DEPLOY_FLEET]
                ?: throw IllegalArgumentException("The deploy fleet link is missing"),
            fleet = fleet
        )

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentFleet(): APIResult<GetOpponentFleetResponseDTO> =
        playersService.getOpponentFleet(
            token = token,
            getOpponentFleetLink = links[Rels.GET_OPPONENT_FLEET]
                ?: throw IllegalArgumentException("The get opponent fleet link is missing")
        )

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
        fireShotsDTO: FireShotsDTO
    ): APIResult<FireShotsResponseDTO> =
        playersService.fireShots(
            token = token,
            fireShotsLink = links[Rels.FIRE_SHOTS]
                ?: throw IllegalArgumentException("The fire shots link is missing"),
            fireShotsDTO = fireShotsDTO
        )

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getOpponentShots(): APIResult<GetOpponentShotsDTO> =
        playersService.getOpponentShots(
            token = token,
            getOpponentShotsLink = links[Rels.GET_OPPONENT_SHOTS]
                ?: throw IllegalArgumentException("The get opponent shots link is missing")
        )
}
