package pt.isel.pdm.battleships.service

import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import pt.isel.pdm.battleships.utils.await

sealed class AuthenticationResult {
    class Success(val token: String) : AuthenticationResult()
    class Failure(val message: String) : AuthenticationResult()
}

/**
 * Represents the service that handles the battleships game.
 */
class UserService(private val apiEndpoint: String) {
    private val httpClient = OkHttpClient()

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

private suspend fun ResponseBody.toJson(): JSONObject =
    withContext(kotlinx.coroutines.Dispatchers.IO) {
        val resString = this@toJson.string()
        JSONObject(resString)
    }

private fun JSONObject.toJsonRequestBody(): RequestBody =
    this.toString()
        .toRequestBody("application/json".toMediaType())
