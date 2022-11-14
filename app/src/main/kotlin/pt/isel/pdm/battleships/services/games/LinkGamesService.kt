package pt.isel.pdm.battleships.services.games

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.GameStateDTO
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.io.IOException

/**
 * TODO Comment
 */
class LinkGamesService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val gamesService: GamesService
) {
    private val token
        get() = sessionManager.accessToken
            ?: throw IllegalStateException("No token available")

    /**
     * Gets all the games
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getAllGames(): APIResult<GamesDTO> =
        gamesService.getAllGames(
            listGamesLink = links[Rels.LIST_GAMES]
                ?: throw IllegalArgumentException("The list games link is missing")
        )

    /**
     * Gets a game by id.
     *
     * @return the result of the get game operation
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(): APIResult<GameDTO> =
        gamesService.getGame(
            token = token,
            gameLink = links[Rels.GAME]
                ?: throw IllegalArgumentException("The game link is missing")
        )

    /**
     * Creates a new game.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(gameConfig: GameConfigDTO): APIResult<SirenEntity<Unit>> =
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
     * @return the result of the matchmaking operation
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(gameConfig: GameConfigDTO): APIResult<MatchmakeDTO> =
        gamesService.matchmake(
            token = token,
            matchmakeLink = links[Rels.MATCHMAKE]
                ?: throw IllegalArgumentException("The matchmake link is missing"),
            gameConfig = gameConfig
        )

    /**
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun joinGame(gameLink: String) {
        // TODO: To be implemented
    }

    /**
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
     * @return the game state
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGameState(): APIResult<GameStateDTO> =
        gamesService.getGameState(
            token = token,
            gameStateLink = links[Rels.GAME_STATE]
                ?: throw IllegalArgumentException("The game state link is missing")
        )
}
