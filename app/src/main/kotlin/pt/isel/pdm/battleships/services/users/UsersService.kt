package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.services.users.dtos.UserDTO
import pt.isel.pdm.battleships.services.users.results.AuthenticationResult
import pt.isel.pdm.battleships.services.users.results.UserResult
import pt.isel.pdm.battleships.utils.await
import pt.isel.pdm.battleships.utils.getBodyOrThrow
import pt.isel.pdm.battleships.utils.toJson
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
    private val httpClient: OkHttpClient,
    private val jsonFormatter: Gson
) {

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     */
    suspend fun login(username: String, password: String): AuthenticationResult {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val req = Request.Builder()
            .url("$apiEndpoint/users/login")
            .post(body = json.toJsonRequestBody())
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) { // TODO: use constants
            AuthenticationResult.Failure(
                error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java)
            )
        } else {
            AuthenticationResult.Success(token = body.toJson().getString("token"))
        }
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
    suspend fun register(email: String, username: String, password: String): AuthenticationResult {
        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("password", password)

        val req = Request.Builder()
            .url("$apiEndpoint/users")
            .post(body = json.toJsonRequestBody())
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            AuthenticationResult.Failure(
                error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java)
            )
        } else {
            AuthenticationResult.Success(token = body.toJson().getString("token"))
        }
    }

    suspend fun getUserByUsername(username: String): UserResult {
        val req = Request.Builder()
            .url("$apiEndpoint/users/$username")
            .get()
            .build()

        val res = httpClient.newCall(req).await()
        val body = res.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (res.code != 200) {
            UserResult.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            UserResult.Success(user = jsonFormatter.fromJson(resJson, UserDTO::class.java))
        }
    }
}
