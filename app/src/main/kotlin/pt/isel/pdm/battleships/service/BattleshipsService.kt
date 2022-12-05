package pt.isel.pdm.battleships.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.service.connection.APIResult
import pt.isel.pdm.battleships.service.connection.UnexpectedResponseException
import pt.isel.pdm.battleships.service.services.games.PlayersService
import pt.isel.pdm.battleships.service.services.home.models.getHome.GetHomeOutput
import pt.isel.pdm.battleships.service.services.users.UsersService
import java.io.IOException

/**
 * The service that handles the battleships game.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonEncoder the JSON encoder used to serialize/deserialize objects
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

    val usersService = UsersService(apiEndpoint, httpClient, jsonEncoder)
    val gamesService = pt.isel.pdm.battleships.service.services.games.GamesService(
        apiEndpoint,
        httpClient,
        jsonEncoder
    )
    val playersService = PlayersService(apiEndpoint, httpClient, jsonEncoder)

    /**
     * Gets the home information.
     *
     * @return the API result of the get home request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getHome(): APIResult<GetHomeOutput> = get(link = HOME_LINK)

    companion object {
        private const val HOME_LINK = "/"
    }
}
