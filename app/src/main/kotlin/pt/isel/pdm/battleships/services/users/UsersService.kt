package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.Result
import pt.isel.pdm.battleships.services.users.dtos.TokenDTO
import pt.isel.pdm.battleships.services.users.dtos.UserDTO
import pt.isel.pdm.battleships.utils.toJsonRequestBody

/**
 * Represents the service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonFormatter the JSON formatter
 */
class UsersService(
    private val apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonFormatter: Gson
) : HTTPService(httpClient, jsonFormatter) {

    private val usersEndpoint = "$apiEndpoint/users"

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun login(username: String, password: String): Result<TokenDTO> {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val req = Request.Builder()
            .url("$usersEndpoint/login")
            .post(body = json.toJsonRequestBody())
            .build()

        return req.getResponseResult()
    }

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun register(email: String, username: String, password: String): Result<TokenDTO> {
        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("password", password)

        val req = Request.Builder()
            .url(usersEndpoint)
            .post(body = json.toJsonRequestBody())
            .build()

        return req.getResponseResult()
    }

    suspend fun getUserByUsername(username: String): Result<UserDTO> {
        val req = Request.Builder()
            .url("$usersEndpoint/$username")
            .get()
            .build()

        return req.getResponseResult()
    }
}
