package pt.isel.pdm.battleships.services.games

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.services.games.models.games.getGameState.GetGameStateOutput
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.services.games.models.games.matchmake.MatchmakeOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.io.IOException

/**
 * The service that handles the games, and keeps track of the links to the endpoints.
 *
 * @property sessionManager the session manager
 * @property links the links to the endpoints
 * @property gamesService the service that handles the games
 */
class LinkGamesService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val gamesService: GamesService
) {
    private val token
        get() = sessionManager.accessToken ?: throw IllegalStateException("No token available")

    /**
     * Gets all the games.
     *
     * @return the API result of the list games request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getAllGames(): APIResult<GetGamesOutput> =
        gamesService.getGames(
            listGamesLink = links[Rels.LIST_GAMES]
                ?: throw IllegalArgumentException("The list games link is missing")
        )

    /**
     * Gets a game by id.
     *
     * @return the result of the get game operation
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(): APIResult<GetGameOutput> =
        gamesService.getGame(
            token = token,
            gameLink = links[Rels.GAME]
                ?: throw IllegalArgumentException("The game link is missing")
        )

    /**
     * Creates a new game.
     *
     * @param gameConfig the game configuration
     *
     * @return the API result of the create game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(gameConfig: GameConfigModel): APIResult<SirenEntity<Unit>> =
        gamesService.createGame(
            token = token,
            createGameLink = links[Rels.CREATE_GAME]
                ?: throw IllegalArgumentException("The create game link is missing"),
            gameConfig = gameConfig
        )

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param gameConfig the DTO with the game's configuration
     *
     * @return the API result of the matchmake request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(gameConfig: GameConfigModel): APIResult<MatchmakeOutput> =
        gamesService.matchmake(
            token = token,
            matchmakeLink = links[Rels.MATCHMAKE]
                ?: throw IllegalArgumentException("The matchmake link is missing"),
            gameConfig = gameConfig
        )

    /**
     * Joins a game.
     *
     * @param gameLink the link to the game
     *
     * @return the API result of the join game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun joinGame(gameLink: String) {
        // TODO: To be implemented
    }

    /**
     * Leaves a game.
     *
     * @param gameLink the link to the game
     *
     * @return the API result of the leave game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun leaveGame(gameLink: String) {
        // TODO: To be implemented
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
        gamesService.getGameState(
            token = token,
            gameStateLink = links[Rels.GAME_STATE]
                ?: throw IllegalArgumentException("The game state link is missing")
        )
}
