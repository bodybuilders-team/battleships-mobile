package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.battleships.services.utils.ErrorDTO
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.await
import pt.isel.pdm.battleships.services.utils.getBodyOrThrow

/**
 * Represents a service that communicates with a HTTP server.
 *
 * @property httpClient the HTTP client used to communicate with the server
 * @property jsonFormatter the JSON formatter used to parse the server responses
 */
abstract class HTTPService(
    protected val httpClient: OkHttpClient,
    protected val jsonFormatter: Gson
) {

    /**
     * Sends a HTTP request to the server and parses the response into a [Result] of the specified type.
     *
     * @receiver the HTTP request to send
     * @return a [Result] of the specified type
     */
    protected suspend inline fun <reified T> Request.getResponseResult(): Result<T> =
        httpClient
            .newCall(request = this)
            .await()
            .getResult()

    /**
     * Parses the response into a [Result] of the specified type.
     *
     * @receiver the HTTP response to parse
     * @return a [Result] of the specified type
     */
    protected inline fun <reified T> Response.getResult(): Result<T> {
        val body = this.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (!this.isSuccessful) {
            Result.Failure(error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java))
        } else {
            Result.Success(dto = jsonFormatter.fromJson(resJson, T::class.java))
        }
    }
}
