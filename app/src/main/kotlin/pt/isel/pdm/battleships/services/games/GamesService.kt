package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.games.models.games.createGame.CreateGameOutput
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.services.games.models.games.getGameState.GetGameStateOutput
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.services.games.models.games.joinGame.JoinGameOutput
import pt.isel.pdm.battleships.services.games.models.games.matchmake.MatchmakeOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * The service that handles the battleships game.
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
     * Gets all the games.
     *
     * @param listGamesLink the link to the list games endpoint
     *
     * @return the API result of the list games request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGames(listGamesLink: String): APIResult<GetGamesOutput> =
        get(link = listGamesLink)

    /**
     * Gets a game by id.
     *
     * @param token the user token for the authentication
     * @param gameLink the game link
     *
     * @return the result of the get game operation
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(token: String, gameLink: String): APIResult<GetGameOutput> =
        get(link = gameLink, token = token)

    /**
     * Creates a new game.
     *
     * @param createGameLink the link to the create game endpoint
     * @param gameConfig the game configuration
     *
     * @return the API result of the create game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(
        token: String,
        createGameLink: String,
        gameConfig: GameConfigModel
    ): APIResult<CreateGameOutput> =
        post(link = createGameLink, token = token, body = gameConfig)

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param token the token of the user that is matchmaking
     * @param matchmakeLink the link to the matchmake endpoint
     * @param gameConfig the DTO with the game's configuration
     *
     * @return the API result of the matchmake request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(
        token: String,
        matchmakeLink: String,
        gameConfig: GameConfigModel
    ): APIResult<MatchmakeOutput> =
        post(link = matchmakeLink, token = token, body = gameConfig)

    /**
     * Joins a game.
     *
     * @param token the token of the user that is joining the game
     * @param joinGameLink the link to the join game endpoint
     *
     * @return the API result of the join game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun joinGame(
        token: String,
        joinGameLink: String
    ): APIResult<JoinGameOutput> =
        post(link = joinGameLink, token = token, body = {}) // TODO EMPTY BODY

    /**
     * Leaves a game.
     *
     * @param token the token of the user that is leaving the game
     * @param gameLink the link to the game
     *
     * @return the API result of the leave game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun leaveGame(
        token: String,
        gameLink: String
    ) {
        // TODO: To be implemented
    }

    /**
     * Gets the state of a game.
     *
     * @param token the token of the user that is matchmaking
     * @param gameStateLink the game state link
     *
     * @return the API result of the get game state request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGameState(
        token: String,
        gameStateLink: String
    ): APIResult<GetGameStateOutput> =
        get(link = gameStateLink, token = token)
}
