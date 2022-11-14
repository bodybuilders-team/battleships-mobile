package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import java.io.IOException
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.GameStateDTO
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON formatter
 */
class GamesService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Gets all the games
     *
     * @param listGamesLink the link to the list games endpoint
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getAllGames(listGamesLink: String): APIResult<GamesDTO> =
        get(listGamesLink)

    /**
     * Gets a game by id.
     *
     * @param token the user token for the authentication
     * @param gameLink the game link
     *
     * @return the result of the get game operation
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(token: String, gameLink: String): APIResult<GameDTO> =
        get(gameLink, token)

    /**
     * Creates a new game.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(
        createGameLink: String,
        gameConfig: GameConfigDTO
    ): APIResult<SirenEntity<Unit>> =
        post(createGameLink, gameConfig)

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param token the token of the user that is matchmaking
     * @param gameConfigDTO the DTO with the game's configuration
     *
     * @return the result of the matchmaking operation
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(
        token: String,
        matchmakeLink: String,
        gameConfigDTO: GameConfigDTO
    ): APIResult<MatchmakeDTO> =
        post(matchmakeLink, token, gameConfigDTO)

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
     * @param token the token of the user that is matchmaking
     * @param gameStateLink the game state link
     *
     * @return the game state
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGameState(
        token: String,
        gameStateLink: String
    ): APIResult<GameStateDTO> =
        get(gameStateLink, token)
}
