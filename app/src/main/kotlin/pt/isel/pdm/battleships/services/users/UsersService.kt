package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.users.dtos.LoginDTO
import pt.isel.pdm.battleships.services.users.dtos.RegisterDTO
import pt.isel.pdm.battleships.services.users.dtos.TokenDTO
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult

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
     * Logs in the user with the given [username] and [password].
     *
     * @param loginLink the link to the login endpoint
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun login(
        loginLink: String,
        username: String,
        password: String
    ): HTTPResult<TokenDTO> =
        post(
            link = loginLink,
            body = LoginDTO(username, password)
        )

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param registerLink the link to the register endpoint
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun register(
        registerLink: String,
        email: String,
        username: String,
        password: String
    ): HTTPResult<TokenDTO> =
        post(
            link = registerLink,
            body = RegisterDTO(email, username, password)
        )

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     */
    suspend fun getUsers(listUsersLink: String): HTTPResult<UsersDTO> =
        get(listUsersLink)
}
