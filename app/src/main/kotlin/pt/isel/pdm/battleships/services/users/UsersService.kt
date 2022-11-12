package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import java.io.IOException
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.users.dtos.AuthenticationOutputDTO
import pt.isel.pdm.battleships.services.users.dtos.LoginDTO
import pt.isel.pdm.battleships.services.users.dtos.RegisterDTO
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON formatter
 */
class UsersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param registerLink the link to the register endpoint
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun register(
        registerLink: String,
        email: String,
        username: String,
        password: String
    ): APIResult<AuthenticationOutputDTO> =
        post(
            link = registerLink,
            body = RegisterDTO(username = username, email = email, password = password)
        )

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param loginLink the link to the login endpoint
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun login(
        loginLink: String,
        username: String,
        password: String
    ): APIResult<AuthenticationOutputDTO> =
        post(
            link = loginLink,
            body = LoginDTO(username, password)
        )

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUsers(listUsersLink: String): APIResult<UsersDTO> =
        get(listUsersLink)

    /**
     * Gets the user home.
     *
     * @param userHomeLink the link to the user home endpoint
     *
     * @return the user home
     */
    suspend fun getUserHome(userHomeLink: String): APIResult<SirenEntity<Unit>> =
        get(userHomeLink)
}
