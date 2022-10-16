package pt.isel.pdm.battleships.services.games

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.GameStateDTO
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO
import pt.isel.pdm.battleships.services.games.dtos.gamesDTOType
import pt.isel.pdm.battleships.services.utils.ErrorDTO
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.await
import pt.isel.pdm.battleships.services.utils.getBodyOrThrow

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
) : HTTPService(httpClient, jsonFormatter) {

    private val gamesEndpoint = "$apiEndpoint/games"

    suspend fun getAllGames(): Result<GamesDTO> {
        val req = Request.Builder()
            .url(gamesEndpoint)
            .get()
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            Result.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            Result.Success(dto = jsonFormatter.fromJson(resJson, gamesDTOType.type))
        }
    }

    /**
     * Gets a game by id.
     *
     * @param token the user token for the authentication
     * @param id the id of the game
     *
     * @return the result of the get game operation
     */
    suspend fun getGame(token: String, id: Int): Result<GameDTO> {
        val req = Request.Builder()
            .url("$gamesEndpoint/$id")
            .header("Authorization", "Bearer $token")
            .get()
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            Result.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            Result.Success(dto = jsonFormatter.fromJson(resJson, GameDTO::class.java))
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
    suspend fun matchmake(token: String, gameConfigDTO: GameConfigDTO): Result<MatchmakeDTO> {
        val req = Request.Builder()
            .url("$gamesEndpoint/matchmake")
            .header("Authorization", "Bearer $token")
            .post(
                body = jsonFormatter
                    .toJson(gameConfigDTO)
                    .toRequestBody("application/json".toMediaType())
            )
            .build()

        return req.getResponseResult()
    }

    suspend fun joinGame(id: Int) {
        // TODO
    }

    suspend fun leaveGame(id: Int) {
        // TODO
    }

    suspend fun getGameState(token: String, id: Int): Result<GameStateDTO> {
        val req = Request.Builder()
            .url("$gamesEndpoint/$id/state")
            .header("Authorization", "Bearer $token")
            .get()
            .build()

        return req.getResponseResult()
    }
}
