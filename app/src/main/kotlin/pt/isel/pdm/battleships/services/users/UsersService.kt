package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.users.dtos.LoginDTO
import pt.isel.pdm.battleships.services.users.dtos.RegisterDTO
import pt.isel.pdm.battleships.services.users.dtos.TokenDTO
import pt.isel.pdm.battleships.services.users.dtos.UserDTO
import pt.isel.pdm.battleships.services.utils.Result

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class UsersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) : HTTPService(apiEndpoint, httpClient, jsonFormatter) {

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun login(username: String, password: String): Result<TokenDTO> =
        post("/users/login", LoginDTO(username, password))

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun register(email: String, username: String, password: String): Result<TokenDTO> =
        post("/users", RegisterDTO(email, username, password))

    suspend fun getUserByUsername(username: String): Result<UserDTO> =
        get("/users/$username")
}
