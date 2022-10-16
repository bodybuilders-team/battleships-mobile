package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.GameStateDTO
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO
import pt.isel.pdm.battleships.services.utils.Result

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class GamesService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) : HTTPService(apiEndpoint, httpClient, jsonFormatter) {

    /**
     * Gets all the games
     */
    suspend fun getAllGames(): Result<GamesDTO> =
        get("/games")

    /**
     * Gets a game by id.
     *
     * @param token the user token for the authentication
     * @param gameLink the game link
     *
     * @return the result of the get game operation
     */
    suspend fun getGame(token: String, gameLink: String): Result<GameDTO> =
        get(gameLink, token)

    suspend fun createGame() {
        // TODO
    }

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param token the token of the user that is matchmaking
     * @param gameConfigDTO the DTO with the game's configuration
     *
     * @return the result of the matchmaking operation
     */
    suspend fun matchmake(
        token: String,
        gameConfigDTO: GameConfigDTO
    ): Result<MatchmakeDTO> =
        post("/games/matchmake", token, gameConfigDTO)

    suspend fun joinGame(gameLink: String) {
        // TODO
    }

    suspend fun leaveGame(gameLink: String) {
        // TODO
    }

    /**
     * Gets the state of a game.
     *
     * @param token the token of the user that is matchmaking
     * @param gameLink the game link
     *
     * @return the game state
     */
    suspend fun getGameState(
        token: String,
        gameLink: String
    ): Result<GameStateDTO> =
        get("$gameLink/state", token)
}
