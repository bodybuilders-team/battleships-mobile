package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.home.dtos.HomeDTO
import pt.isel.pdm.battleships.services.players.PlayersService
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.utils.Result

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonFormatter the JSON formatter
 *
 */
class BattleshipsService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) : HTTPService(apiEndpoint, httpClient, jsonFormatter) {
    val usersService = UsersService(apiEndpoint, httpClient, jsonFormatter)
    val gamesService = GamesService(apiEndpoint, httpClient, jsonFormatter)
    val playersService = PlayersService(apiEndpoint, httpClient, jsonFormatter)

    suspend fun getHome(): Result<HomeDTO> =
        get("/home")
}
