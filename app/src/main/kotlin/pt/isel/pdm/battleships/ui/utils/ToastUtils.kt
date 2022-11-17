package pt.isel.pdm.battleships.ui.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Defines the duration of a [Toast] message.
 *
 * @property duration the duration of the [Toast] message
 */
enum class ToastDuration(val duration: Int) {
    SHORT(duration = Toast.LENGTH_SHORT),
    LONG(duration = Toast.LENGTH_LONG)
}

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
