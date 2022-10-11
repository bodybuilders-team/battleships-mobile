package pt.isel.pdm.battleships.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pt.isel.pdm.battleships.utils.await
import pt.isel.pdm.battleships.utils.toJson
import pt.isel.pdm.battleships.utils.toJsonRequestBody

sealed class AuthenticationResult {
    class Success(val token: String) : AuthenticationResult()
    class Failure(val message: String) : AuthenticationResult()
}

/**
 * Represents the service that handles the battleships game.
 */
class UserService(
    private val apiEndpoint: String,
    private val httpClient: OkHttpClient,
    private val jsonFormatter: Gson
) {

    /**
     * Logs in the user with the given [username] and [password].
     * @return The authentication result.
     */
    suspend fun login(username: String, password: String): AuthenticationResult {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val req = Request.Builder()
            .url("$apiEndpoint/users/login")
            .post(
                json.toJsonRequestBody()
            )
            .build()

        val res = httpClient.newCall(req).await()
        val resJson = res.body?.toJson() ?: throw IllegalStateException("Response body is null")

        return if (res.code != 200) {
            AuthenticationResult.Failure(
                resJson.getString("message")
            )
        } else {
            AuthenticationResult.Success(resJson.getString("token"))
        }
    }

    /**
     * Registers the user with the given [email], [username] and [password].
     * @return The authentication result.
     */
    suspend fun register(email: String, username: String, password: String): AuthenticationResult {
        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("password", password)

        val req = Request.Builder()
            .url("$apiEndpoint/users")
            .post(
                json.toJsonRequestBody()
            )
            .build()

        val res = httpClient.newCall(req).await()
        val resJson = res.body?.toJson() ?: throw IllegalStateException("Response body is null")

        if (res.code != 200) {
            return AuthenticationResult.Failure(
                resJson.getString("message")
            )
        }

        return AuthenticationResult.Success(resJson.getString("token"))
    }

    suspend fun getUserByUsername(username: String) {
        // TODO
    }
}
