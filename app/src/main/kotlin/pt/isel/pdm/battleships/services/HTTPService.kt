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

abstract class HTTPService(
    val apiEndpoint: String,
    val httpClient: OkHttpClient,
    val jsonFormatter: Gson
) {

    suspend inline fun <reified T> Request.getResponseResult(): Result<SirenEntity<T>> {
        val res = httpClient.newCall(this).await()

        return res.getResult()
    }

    inline fun <reified T> Response.getResult(): Result<SirenEntity<T>> {
        val body = this.getBodyOrThrow()
        val resJson = JsonReader(body.charStream())

        return if (!this@getResult.isSuccessful) {
            Result.Failure(
                error = jsonFormatter.fromJson(resJson)
            )
        } else {
            Result.Success(
                data = jsonFormatter.fromJson(resJson, SirenEntity.getType<T>().type)
            )
        }
    }

    suspend inline fun <reified T> get(link: String): Result<SirenEntity<T>> {
        val req = Request.Builder()
            .url(apiEndpoint + link)
            .build()

        return req.getResponseResult()
    }

    suspend inline fun <reified T> get(
        link: String,
        token: String
    ): Result<SirenEntity<T>> {
        val req = Request.Builder()
            .url(apiEndpoint + link)
            .header("Authorization", "Bearer $token")
            .build()

        return req.getResponseResult()
    }

    suspend inline fun <reified T> post(
        link: String,
        body: Any
    ): Result<SirenEntity<T>> {
        val req = Request.Builder()
            .url(apiEndpoint + link)
            .post(
                jsonFormatter.toJson(body).toRequestBody(
                    "application/json".toMediaType()
                )
            )
            .build()

        return req.getResponseResult()
    }

    suspend inline fun <reified T> post(
        link: String,
        token: String,
        body: Any
    ): Result<SirenEntity<T>> {
        val req = Request.Builder()
            .url(apiEndpoint + link)
            .header("Authorization", "Bearer $token")
            .post(
                jsonFormatter.toJson(body).toRequestBody(
                    "application/json".toMediaType()
                )
            )
            .build()

        return req.getResponseResult()
    }
}
