package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.players.PlayersService
import pt.isel.pdm.battleships.services.users.UsersService

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonFormatter the JSON formatter
 *
 * @property usersService the service used to handle the users
 * @property gamesService the service used to handle the games
 * @property playersService the service used to handle the players
 */
class BattleshipsService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) {
    val usersService = UsersService(apiEndpoint, httpClient, jsonFormatter)
    val gamesService = GamesService(apiEndpoint, httpClient, jsonFormatter)
    val playersService = PlayersService(apiEndpoint, httpClient, jsonFormatter)
}
