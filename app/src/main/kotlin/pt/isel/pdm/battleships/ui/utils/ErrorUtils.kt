package pt.isel.pdm.battleships.ui.utils // ktlint-disable filename

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.suspendCancellableCoroutine
import pt.isel.pdm.battleships.services.UnexpectedResponseException
import java.io.IOException
import kotlin.coroutines.resume

/**
 * Shows a toast message to the user.
 *
 * @param errorMessage the message to be shown
 * @param toastDuration the duration of the toast
 * @param onDismissed the callback to be invoked when the toast is dismissed
 */
suspend fun Context.showToast(
    errorMessage: String,
    toastDuration: ToastDuration = ToastDuration.SHORT,
    onDismissed: (suspend () -> Unit)? = null
) {
    val toast = Toast.makeText(
        /* context = */ this,
        /* text = */ errorMessage,
        /* duration = */ toastDuration.duration
    )

    suspendCancellableCoroutine<Unit> { continuation ->
        toast.addCallback(
            object : Toast.Callback() {
                override fun onToastHidden() {
                    continuation.resume(Unit)
                }
            }
        )
        toast.show()
        continuation.invokeOnCancellation { toast.cancel() }
    }

    if (onDismissed != null) onDismissed()
}

/**
 * Tries to execute the given request and returns a [HTTPResult] with the result.
 *
 * If the request fails, the returned [HTTPResult] will be a [HTTPResult.Failure]
 * with the error message.
 *
 * @param executeRequest the request to execute
 * @return the result of the request
 */
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
