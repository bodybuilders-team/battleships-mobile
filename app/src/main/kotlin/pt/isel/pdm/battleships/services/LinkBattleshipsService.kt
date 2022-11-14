package pt.isel.pdm.battleships.services

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.LinkGamesService
import pt.isel.pdm.battleships.services.games.LinkPlayersService
import pt.isel.pdm.battleships.services.home.dtos.HomeDTO
import pt.isel.pdm.battleships.services.users.LinkUsersService
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonEncoder the JSON formatter
 *
 * @property usersService the service that handles the users
 * @property gamesService the service that handles the games
 * @property playersService the service that handles the players
 */
class LinkBattleshipsService(
    links: Map<String, String>,
    sessionManager: SessionManager,
    private val battleshipsService: BattleshipsService
) {
    val links = links.toMutableMap()

    val usersService = LinkUsersService(
        links = this.links,
        usersService = battleshipsService.usersService
    )
    val gamesService = LinkGamesService(
        sessionManager = sessionManager,
        links = this.links,
        gamesService = battleshipsService.gamesService
    )
    val playersService =
        LinkPlayersService(
            sessionManager = sessionManager,
            links = this.links,
            playersService = battleshipsService.playersService
        )

    /**
     * Gets the home information.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getHome(): APIResult<HomeDTO> = battleshipsService.getHome()
}
