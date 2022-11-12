package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.PlayersService
import pt.isel.pdm.battleships.services.home.dtos.HomeDTO
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * Represents the service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonEncoder the JSON formatter
 *
 * @property usersService the service that handles the users
 * @property gamesService the service that handles the games
 * @property playersService the service that handles the players
 */
class BattleshipsService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    // TODO: Implement local mode
    /**
     * Used to identify how implementations SHOULD behave:
     *
     * @property FORCE_REMOTE is used to indicate that the operation MUST try to access
     * the remote data source
     * @property FORCE_LOCAL is usd to indicate that the operation SHOULD only use the
     * the local version of the data, if available
     * @property AUTO states that the selection of which data to use is left to the
     * implementation.
     *//*
    enum class Mode {
        FORCE_REMOTE,
        FORCE_LOCAL,
        AUTO
    }*/

    val usersService = UsersService(apiEndpoint, httpClient, jsonEncoder)
    val gamesService = GamesService(apiEndpoint, httpClient, jsonEncoder)
    val playersService = PlayersService(apiEndpoint, httpClient, jsonEncoder)

    /**
     * Gets the home information.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getHome(): APIResult<HomeDTO> = get(link = "/")
}
