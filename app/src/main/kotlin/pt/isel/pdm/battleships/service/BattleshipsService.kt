package pt.isel.pdm.battleships.service

import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * Represents the service that handles the battleships game.
 *
 * @property usersService The service used to handle the users.
 * @property gamesService The service used to handle the games.
 * @property playersService The service used to handle the players.
 */
class BattleshipsService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) {

    val usersService: UserService = UserService(apiEndpoint, httpClient, jsonFormatter)

    val gamesService: GameService = GameService(apiEndpoint, httpClient, jsonFormatter)

    val playersService: PlayersService = PlayersService(apiEndpoint, httpClient, jsonFormatter)
}
