package pt.isel.pdm.battleships.service

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.pdm.battleships.utils.await

sealed class MatchmakeResult {
    class Success(val dto: MatchmakeDTO) : MatchmakeResult()
    class Failure(val error: ErrorDTO) : MatchmakeResult()
}

sealed class GameResult {
    class Success(val dto: GameDTO) : GameResult()
    class Failure(val error: ErrorDTO) : GameResult()
}

data class ErrorDTO(val message: String)

data class ShipTypeDTO(
    val shipName: String,
    val size: Int,
    val quantity: Int,
    val points: Int
)

data class GameConfigDTO(
    val gridSize: Int,
    val maxTimeForLayoutPhase: Int,
    val shotsPerRound: Int,
    val maxTimePerShot: Int,
    val shipTypes: List<ShipTypeDTO>
)

data class GameStateDTO(
    val phase: String,
    val phaseEndTime: Long,
    val round: Int?,
    val turn: String?,
    val winner: String?
)

data class PlayerDTO(
    val username: String,
    val points: Int
)

data class GameDTO(
    val id: Int,
    val name: String,
    val creator: String,
    val config: GameConfigDTO,
    val state: GameStateDTO,
    val players: List<PlayerDTO>
)

data class MatchmakeDTO(
    val wasCreated: Boolean,
    val gameId: Int
)

/**
 * Represents the service that handles the battleships game.
 */
class GameService(
    private val apiEndpoint: String,
    private val httpClient: OkHttpClient,
    private val jsonFormatter: Gson
) {

    private val gamesEndpoint = "$apiEndpoint/games"

    fun getAllGames() {
        // TODO
    }

    suspend fun getGameById(token: String, id: Int): GameResult {
        val req = Request.Builder()
            .url("$gamesEndpoint/$id")
            .header("Authorization", "Bearer $token")
            .get()
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.body ?: throw IllegalStateException("Response body is null")
        val resJson = JsonReader(body.charStream())

        if (res.code != 200) {
            return GameResult.Failure(
                jsonFormatter.fromJson(resJson, ErrorDTO::class.java)
            )
        }

        return GameResult.Success(
            jsonFormatter.fromJson(resJson, GameDTO::class.java)
        )
    }

    suspend fun createGame() {
        // TODO
    }

    suspend fun matchmake(token: String, gameConfigDTO: GameConfigDTO): MatchmakeResult {
        val req = Request.Builder()
            .url("$gamesEndpoint/matchmake")
            .header("Authorization", "Bearer $token")
            .post(
                jsonFormatter.toJson(gameConfigDTO)
                    .toRequestBody(
                        "application/json".toMediaType()
                    )
            )
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.body ?: throw IllegalStateException("Response body is null")
        val resJson = JsonReader(body.charStream())

        if (res.code != 200) {
            return MatchmakeResult.Failure(
                jsonFormatter.fromJson(resJson, ErrorDTO::class.java)
            )
        }

        return MatchmakeResult.Success(
            jsonFormatter.fromJson(resJson, MatchmakeDTO::class.java)
        )
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
