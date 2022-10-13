package pt.isel.pdm.battleships.services

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.utils.await
import pt.isel.pdm.battleships.utils.getBodyOrThrow

/**
 * HTTP Response result.
 */
sealed class Result<T> {

    /**
     * The response was successful.
     *
     * @property dto the response DTO
     */
    class Success<T>(val dto: T) : Result<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the error DTO
     */
    class Failure<T>(val error: ErrorDTO) : Result<T>()
}

abstract class HTTPService(
    protected val httpClient: OkHttpClient,
    protected val jsonFormatter: Gson
) {

    protected suspend inline fun <reified T> Request.getResponseResult(): Result<T> {
        val res = httpClient.newCall(this).await()

        return res.getResult()
    }

    protected inline fun <reified T> Response.getResult(): Result<T> {
        val body = this.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (!this.isSuccessful) {
            Result.Failure(
                error = jsonFormatter.fromJson(resJson, ErrorDTO::class.java)
            )
        } else {
            Result.Success(
                dto =
                jsonFormatter.fromJson(resJson, T::class.java)
            )
        }
    }
}
