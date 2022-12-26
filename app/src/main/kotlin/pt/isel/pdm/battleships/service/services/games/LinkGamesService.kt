package pt.isel.pdm.battleships.service.services.games

import pt.isel.pdm.battleships.service.connection.APIResult
import pt.isel.pdm.battleships.service.connection.UnexpectedResponseException
import pt.isel.pdm.battleships.service.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.service.services.games.models.games.createGame.CreateGameInput
import pt.isel.pdm.battleships.service.services.games.models.games.createGame.CreateGameOutput
import pt.isel.pdm.battleships.service.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.service.services.games.models.games.getGame.GetGameOutputModel
import pt.isel.pdm.battleships.service.services.games.models.games.getGameState.GetGameStateOutput
import pt.isel.pdm.battleships.service.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.service.services.games.models.games.joinGame.JoinGameOutput
import pt.isel.pdm.battleships.service.services.games.models.games.leaveGame.LeaveGameOutput
import pt.isel.pdm.battleships.service.services.games.models.games.matchmake.MatchmakeOutput
import pt.isel.pdm.battleships.service.services.users.LinkUsersService
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels
import java.io.IOException

/**
 * The service that handles the games, and keeps track of the links to the endpoints.
 *
 * @property sessionManager the manager used to handle the user session
 * @property links the links to the endpoints
 * @property gamesService the service that handles the games
 */
class LinkGamesService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val gamesService: GamesService,
    private val usersService: LinkUsersService
) {
    private val accessToken
        get() = sessionManager.accessToken
            ?: throw IllegalStateException("No access token available")

    private val refreshToken
        get() = sessionManager.refreshToken
            ?: throw IllegalStateException("No refresh token available")

    /**
     * Gets all the games.
     *
     * @return the API result of the get games request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGames(): APIResult<GetGamesOutput> {
        val getGamesResult = gamesService.getGames(
            listGamesLink = links[Rels.LIST_GAMES]
                ?: throw IllegalStateException("The list games link is missing")
        )

        if (getGamesResult !is APIResult.Success)
            return getGamesResult

        getGamesResult.data.embeddedSubEntities<GetGameOutputModel>(Rels.ITEM, Rels.GAME)
            .forEach { entity ->
                val id = entity.properties?.id
                    ?: throw IllegalArgumentException("The properties are missing")

                links["${Rels.GAME}-$id"] = entity.getLink(Rels.SELF).href.path
                links["${Rels.JOIN_GAME}-$id"] = entity.getAction(Rels.JOIN_GAME).href.path
            }

        return getGamesResult
    }

    /**
     * Creates a new game.
     *
     * @param name the name of the game
     * @param config the game configuration
     *
     * @return the API result of the create game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(name: String, config: GameConfigModel): APIResult<CreateGameOutput> {
        val createGameResult = executeRequestRefreshingToken {
            gamesService.createGame(
                token = accessToken,
                createGameLink = links[Rels.CREATE_GAME]
                    ?: throw IllegalStateException("The create game link is missing"),
                createGameInput = CreateGameInput(name = name, config = config)
            )
        }

        if (createGameResult !is APIResult.Success)
            return createGameResult

        links[Rels.GAME] = createGameResult.data.embeddedLinks(Rels.GAME).single().href.path
        links[Rels.GAME_STATE] =
            createGameResult.data.embeddedLinks(Rels.GAME_STATE).single().href.path

        return createGameResult
    }

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param gameConfig the game's configuration
     *
     * @return the API result of the matchmake request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(gameConfig: GameConfigModel): APIResult<MatchmakeOutput> {
        val matchmakeResult = executeRequestRefreshingToken {
            gamesService.matchmake(
                token = accessToken,
                matchmakeLink = links[Rels.MATCHMAKE]
                    ?: throw IllegalStateException("The matchmake link is missing"),
                gameConfig = gameConfig
            )
        }

        if (matchmakeResult !is APIResult.Success)
            return matchmakeResult

        links[Rels.GAME] = matchmakeResult.data.embeddedLinks(Rels.GAME).single().href.path
        links[Rels.GAME_STATE] =
            matchmakeResult.data.embeddedLinks(Rels.GAME_STATE).single().href.path

        return matchmakeResult
    }

    /**
     * Gets the game information.
     *
     * @return the result of the get game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(): APIResult<GetGameOutput> {
        val getGameResult = executeRequestRefreshingToken {
            gamesService.getGame(
                token = accessToken,
                gameLink = links[Rels.GAME]
                    ?: throw IllegalStateException("The game link is missing")
            )
        }

        if (getGameResult !is APIResult.Success)
            return getGameResult

        links[Rels.GAME_STATE] =
            getGameResult.data.embeddedLinks(Rels.GAME_STATE).single().href.path

        links.putAll(getGameResult.data.getActionLinks())

        return getGameResult
    }

    /**
     * Gets the state of a game.
     *
     * @return the API result of the get game state request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGameState(): APIResult<GetGameStateOutput> =
        executeRequestRefreshingToken {
            gamesService.getGameState(
                token = accessToken,
                gameStateLink = links[Rels.GAME_STATE]
                    ?: throw IllegalStateException("The game state link is missing")
            )
        }

    /**
     * Joins a game.
     *
     * @param joinGameLink the link to the join game endpoint
     *
     * @return the API result of the join game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun joinGame(joinGameLink: String): APIResult<JoinGameOutput> {
        val joinGameResult = executeRequestRefreshingToken {
            gamesService.joinGame(
                token = accessToken,
                joinGameLink = joinGameLink
            )
        }

        if (joinGameResult !is APIResult.Success)
            return joinGameResult

        links[Rels.GAME] = joinGameResult.data.embeddedLinks(Rels.GAME).single().href.path
        links[Rels.GAME_STATE] =
            joinGameResult.data.embeddedLinks(Rels.GAME_STATE).single().href.path

        return joinGameResult
    }

    /**
     * Leaves a game.
     *
     * @return the API result of the leave game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun leaveGame(): APIResult<LeaveGameOutput> {
        val leaveGameResult = executeRequestRefreshingToken {
            gamesService.leaveGame(
                token = accessToken,
                leaveGameLink = links[Rels.LEAVE_GAME]
                    ?: throw IllegalStateException("The leave game link is missing")
            )
        }

        if (leaveGameResult !is APIResult.Success)
            return leaveGameResult

        links.remove(Rels.GAME)
        links.remove(Rels.GAME_STATE)

        return leaveGameResult
    }

    /**
     * Executes a request, refreshing the access token.
     *
     * @param T the type of the API result
     * @param request the request to execute
     *
     * @return the result of the request
     */
    private suspend fun <T> executeRequestRefreshingToken(request: suspend () -> T): T {
        val refreshTokenResult = usersService.refreshToken(refreshToken)

        if (refreshTokenResult !is APIResult.Success)
            throw IllegalStateException("The refresh token request failed")

        val refreshTokenProperties = refreshTokenResult.data.properties
            ?: throw IllegalStateException("The properties are missing")

        sessionManager.setSession(
            accessToken = refreshTokenProperties.accessToken,
            refreshToken = refreshTokenProperties.refreshToken,
            username = sessionManager.username!!,
            userHomeLink = links[Rels.USER_HOME]
                ?: throw IllegalStateException("The user home link is missing")
        )

        return request()
    }
}
