package pt.isel.pdm.battleships.services.utils

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import pt.isel.pdm.battleships.services.HTTPService.Companion.applicationJsonMediaType
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Sends a request through the HTTP client and parses the response into a [T].
 *
 * @receiver the HTTP request to send
 * @param parseResponse the function that parses the response into a [T]
 * @return the parsed response
 */
suspend fun <T> Request.send(okHttpClient: OkHttpClient, parseResponse: (Response) -> T): T =
    suspendCancellableCoroutine { continuation ->
        val call = okHttpClient.newCall(request = this)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    continuation.resume(parseResponse(response))
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        })

        continuation.invokeOnCancellation { call.cancel() }
    }

/**
 * Converts the [ResponseBody] to a [JSONObject].
 *
 * @receiver the [ResponseBody] to be converted
 * @return the json object
 */
suspend fun ResponseBody.toJson(): JSONObject =
    withContext(Dispatchers.IO) {
        val resString = this@toJson.string()
        JSONObject(resString)
    }

/**
 * Converts the [JSONObject] to a [RequestBody].
 *
 * @receiver the json object
 * @return the request body
 */
fun JSONObject.toJsonRequestBody(): RequestBody = this
    .toString()
    .toRequestBody(applicationJsonMediaType)

/**
 * Gets the [ResponseBody] from the [Response] body.
 *
 * @receiver the response
 *
 * @return the response body
 * @throws IllegalStateException if the response body is null
 */
fun Response.getBodyOrThrow(): ResponseBody =
    body ?: throw IllegalStateException("Response body is null")

/**
 * Parses an object of type [T] from the [json] stream.
 *
 * @receiver the Gson instance
 * @param json the json stream
 *
 * @return the parsed object
 */
inline fun <reified T> Gson.fromJson(json: JsonReader): T = fromJson(json, T::class.java)
