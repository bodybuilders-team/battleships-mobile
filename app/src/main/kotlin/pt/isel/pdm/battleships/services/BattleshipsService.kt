package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.PlayersService
import pt.isel.pdm.battleships.services.home.models.getHome.GetHomeOutput
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * The service that handles the battleships game.
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

    val usersService = UsersService(apiEndpoint, httpClient, jsonEncoder)
    val gamesService = GamesService(apiEndpoint, httpClient, jsonEncoder)
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
