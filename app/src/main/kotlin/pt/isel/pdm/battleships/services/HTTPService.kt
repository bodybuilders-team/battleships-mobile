package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.await
import pt.isel.pdm.battleships.services.utils.fromJson
import pt.isel.pdm.battleships.services.utils.getBodyOrThrow
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents a service that communicates with a HTTP server.
 *
 * @property apiEndpoint the base URL of the API
 * @property httpClient the HTTP client used to communicate with the server
 * @property jsonFormatter the JSON formatter used to parse the server responses
 */
abstract class HTTPService(
    val apiEndpoint: String,
    val httpClient: OkHttpClient,
    val jsonFormatter: Gson
) {

    /**
     * Sends a HTTP request to the server and parses the response into a [Result] of a
     * [SirenEntity] with the specified type.
     *
     * @receiver the HTTP request to send
     * @return the result of the request
     */
    suspend inline fun <reified T> Request.getResponseResult(): Result<SirenEntity<T>> =
        httpClient
            .newCall(request = this)
            .await()
            .getResult()

    /**
     * Parses the response into a [Result] of a [SirenEntity] with the specified type.
     *
     * @receiver the HTTP response to parse
     * @return the result of the response
     */
    inline fun <reified T> Response.getResult(): Result<SirenEntity<T>> {
        val body = this.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (!this@getResult.isSuccessful) {
            Result.Failure(error = jsonFormatter.fromJson(resJson))
        } else {
            Result.Success(data = jsonFormatter.fromJson(resJson, SirenEntity.getType<T>().type))
        }
    }

    /**
     * Sends a GET request to the specified link.
     *
     * @param link the link to send the request to
     * @return the result of the request
     */
    suspend inline fun <reified T> get(link: String): Result<SirenEntity<T>> =
        Request.Builder()
            .url(apiEndpoint + link)
            .build().getResponseResult()

    /**
     * Sends a GET request to the specified link with a token in the header.
     *
     * @param link the link to send the request to
     * @param token the token to send in the header
     *
     * @return the result of the request
     */
    suspend inline fun <reified T> get(link: String, token: String): Result<SirenEntity<T>> =
        Request.Builder()
            .url(url = apiEndpoint + link)
            .header(name = AUTHORIZATION_HEADER, value = "Bearer $token")
            .build()
            .getResponseResult()

    /**
     * Sends a POST request to the specified link with the specified body.
     *
     * @param link the link to send the request to
     * @param body the body to send in the request
     *
     * @return the result of the request
     */
    suspend inline fun <reified T> post(link: String, body: Any): Result<SirenEntity<T>> =
        Request.Builder()
            .url(url = apiEndpoint + link)
            .post(
                body = jsonFormatter
                    .toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponseResult()

    /**
     * Sends a POST request to the specified link with the specified body and a token in the header.
     *
     * @param link the link to send the request to
     * @param token the token to send in the header
     * @param body the body to send in the request
     *
     * @return the result of the request
     */
    suspend inline fun <reified T> post(
        link: String,
        token: String,
        body: Any
    ): Result<SirenEntity<T>> =
        Request.Builder()
            .url(url = apiEndpoint + link)
            .header(name = AUTHORIZATION_HEADER, value = "Bearer $token")
            .post(
                body = jsonFormatter
                    .toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponseResult()

    companion object {
        private const val APPLICATION_JSON = "application/json"
        val applicationJsonMediaType = APPLICATION_JSON.toMediaType()

        const val AUTHORIZATION_HEADER = "Authorization"
    }
}
