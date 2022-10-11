package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO
import pt.isel.pdm.battleships.services.games.results.GameResult
import pt.isel.pdm.battleships.services.games.results.MatchmakeResult
import pt.isel.pdm.battleships.utils.await
import pt.isel.pdm.battleships.utils.getBodyOrThrow

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class GamesService(
    apiEndpoint: String,
    private val httpClient: OkHttpClient,
    private val jsonFormatter: Gson
) {

    private val gamesEndpoint = "$apiEndpoint/games"

    fun getAllGames() {
        // TODO
    }

    /**
     * Gets a game by id.
     *
     * @param token the user token for the authentication
     * @param id the id of the game
     *
     * @return the result of the get game operation
     */
    suspend fun getGame(token: String, id: Int): GameResult {
        val req = Request.Builder()
            .url("$gamesEndpoint/$id")
            .header("Authorization", "Bearer $token")
            .get()
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            GameResult.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            GameResult.Success(game = jsonFormatter.fromJson(resJson, GameDTO::class.java))
        }
    }

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
    suspend fun matchmake(token: String, gameConfigDTO: GameConfigDTO): MatchmakeResult {
        val req = Request.Builder()
            .url("$gamesEndpoint/matchmake")
            .header("Authorization", "Bearer $token")
            .post(
                body = jsonFormatter
                    .toJson(gameConfigDTO)
                    .toRequestBody("application/json".toMediaType())
            )
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            MatchmakeResult.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            MatchmakeResult.Success(dto = jsonFormatter.fromJson(resJson, MatchmakeDTO::class.java))
        }
    }

    suspend fun joinGame(id: Int) {
        // TODO
    }

    suspend fun leaveGame(id: Int) {
        // TODO
    }

    suspend fun getGameState(id: Int) {
        // TODO
    }
}
