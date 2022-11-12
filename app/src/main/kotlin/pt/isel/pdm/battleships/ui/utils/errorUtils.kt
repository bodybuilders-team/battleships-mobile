package pt.isel.pdm.battleships.ui.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.suspendCancellableCoroutine
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import java.io.IOException
import kotlin.coroutines.resume

enum class ToastDuration(val duration: Int) {
    SHORT(Toast.LENGTH_SHORT),
    LONG(Toast.LENGTH_LONG)
}

suspend fun Context.showToast(
    errorMessage: String,
    toastDuration: ToastDuration = ToastDuration.SHORT,
    onDismissed: (suspend () -> Unit)? = null
) {
    val toast = Toast.makeText(
        this,
        errorMessage,
        toastDuration.duration
    )

    suspendCancellableCoroutine<Unit> { continuation ->
        toast.addCallback(object : Toast.Callback() {
            override fun onToastHidden() {
                continuation.resume(Unit)
            }
        })
        toast.show()
        continuation.invokeOnCancellation { toast.cancel() }
    }

    if (onDismissed != null)
        onDismissed()
}

sealed class HTTPResult<T> {
    class Success<T>(val data: T) : HTTPResult<T>()
    class Failure<T>(val error: String) : HTTPResult<T>()
}

suspend fun <T> tryExecuteHttpRequest(
    executeRequest: suspend () -> T
): HTTPResult<T> =
    try {
        HTTPResult.Success(executeRequest())
    } catch (e: IOException) {
        HTTPResult.Failure("Could not connect to the server.")
    } catch (e: UnexpectedResponseException) {
        HTTPResult.Failure("Server sent an unexpected response.")
    }
