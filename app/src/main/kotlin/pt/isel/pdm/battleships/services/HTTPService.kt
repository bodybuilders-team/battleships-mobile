package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.Problem.Companion.problemMediaType
import pt.isel.pdm.battleships.services.utils.fromJson
import pt.isel.pdm.battleships.services.utils.getBodyOrThrow
import pt.isel.pdm.battleships.services.utils.send
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity.Companion.sirenMediaType
import java.io.IOException

/**
 * Represents a service that communicates with a HTTP server.
 *
 * @property apiEndpoint the base URL of the API
 * @property httpClient the HTTP client used to communicate with the server
 * @property jsonEncoder the JSON formatter used to parse the server responses
 */
abstract class HTTPService(
    val apiEndpoint: String,
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson
) {

    /**
     * Sends a HTTP request to the server and parses the response into a [APIResult] of a
     * [SirenEntity] with the specified type.
     *
     * @receiver the HTTP request to send
     *
     * @return the result of the request
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend inline fun <reified T> Request.getResponseResult(): APIResult<SirenEntity<T>> =
        this.send(httpClient) { response ->
            val body = response.getBodyOrThrow()
            val contentType = body.contentType()
            val resJson = JsonReader(body.charStream())

            try {
                when {
                    response.isSuccessful && contentType == sirenMediaType ->
                        APIResult.Success(
                            data = jsonEncoder.fromJson(
                                resJson,
                                SirenEntity.getType<T>().type
                            )
                        )
                    !response.isSuccessful && contentType == problemMediaType ->
                        APIResult.Failure(error = jsonEncoder.fromJson(resJson))
                    else -> throw UnexpectedResponseException(response)
                }
            } catch (e: JsonSyntaxException) {
                throw UnexpectedResponseException(response)
            }
        }

    /**
     * Sends a GET request to the specified link.
     *
     * @param link the link to send the request to
     *
     * @return the result of the request
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend inline fun <reified T> get(link: String): APIResult<SirenEntity<T>> =
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
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend inline fun <reified T> get(link: String, token: String): APIResult<SirenEntity<T>> =
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
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend inline fun <reified T> post(link: String, body: Any): APIResult<SirenEntity<T>> =
        Request.Builder()
            .url(url = apiEndpoint + link)
            .post(
                body = jsonEncoder
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
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend inline fun <reified T> post(
        link: String,
        token: String,
        body: Any
    ): APIResult<SirenEntity<T>> =
        Request.Builder()
            .url(url = apiEndpoint + link)
            .header(name = AUTHORIZATION_HEADER, value = "Bearer $token")
            .post(
                body = jsonEncoder
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

/**
 * Represents an exception thrown when the server responds with an unexpected response.
 *
 * @property response the response that caused the exception
 *
 */
class UnexpectedResponseException(val response: Response) :
    Exception("Unexpected ${response.code} response from the server.")
