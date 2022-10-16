package pt.isel.pdm.battleships.services.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Suspends the current coroutine until the [Call] completes.
 *
 * @receiver the [Call] to be executed
 * @return the [Response] of the [Call]
 */
suspend fun Call.await(): Response =
    suspendCancellableCoroutine { continuation ->
        enqueue(
            responseCallback = object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    if (!this@await.isCanceled()) continuation.resumeWithException(e)
                }
            }
        )

        continuation.invokeOnCancellation { this.cancel() }
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
    .toRequestBody("application/json".toMediaType())

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
