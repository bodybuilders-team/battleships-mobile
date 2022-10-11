package pt.isel.pdm.battleships.utils

import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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

/**
 * Suspends the current coroutine until the [Call] completes.
 * @return The [Response] of the [Call].
 */
suspend fun Call.await(): Response =
    suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!this@await.isCanceled()) {
                    continuation.resumeWithException(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }
        })

        continuation.invokeOnCancellation {
            this.cancel()
        }
    }

/**
 * Converts the [ResponseBody] to a [JSONObject].
 *
 */
suspend fun ResponseBody.toJson(): JSONObject =
    withContext(Dispatchers.IO) {
        val resString = this@toJson.string()
        JSONObject(resString)
    }

/**
 * Converts the [JSONObject] to a [RequestBody].
 */
fun JSONObject.toJsonRequestBody(): RequestBody =
    this.toString()
        .toRequestBody("application/json".toMediaType())
