package pt.isel.pdm.battleships.services

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.LinkGamesService
import pt.isel.pdm.battleships.services.games.LinkPlayersService
import pt.isel.pdm.battleships.services.home.models.getHome.GetHomeOutput
import pt.isel.pdm.battleships.services.users.LinkUsersService
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * The service that handles the battleships game, and keeps track of the journey links.
 *
 * @param links the links to the battleships game
 * @param sessionManager the session manager
 * @property battleshipsService the service that handles the battleships game
 *
 * @property usersService the service that handles the users
 * @property gamesService the service that handles the games
 * @property playersService the service that handles the players
 */
class LinkBattleshipsService(
    links: Map<String, String>,
    private val battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) {
    private val _links = links.toMutableMap()
    val links
        get() = _links.toMap()

    val usersService = LinkUsersService(
        links = this._links,
        usersService = battleshipsService.usersService
    )

    val gamesService = LinkGamesService(
        sessionManager = sessionManager,
        links = this._links,
        gamesService = battleshipsService.gamesService
    )

    val playersService = LinkPlayersService(
        sessionManager = sessionManager,
        links = this._links,
        playersService = battleshipsService.playersService
    )

    /**
     * Gets the home information.
     *
     * @return the API result with the home information
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getHome(): APIResult<GetHomeOutput> {
        val getHomeResult = battleshipsService.getHome()

        if (getHomeResult !is APIResult.Success)
            return getHomeResult

        _links.putAll(getHomeResult.data.getActionLinks())

        return getHomeResult
    }
}
