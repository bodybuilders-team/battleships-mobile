package pt.isel.pdm.battleships.ui.utils

import android.widget.Toast

/**
 * Defines the duration of a [Toast] message.
 *
 * @property duration the duration of the [Toast] message
 */
enum class ToastDuration(val duration: Int) {
    SHORT(Toast.LENGTH_SHORT),
    LONG(Toast.LENGTH_LONG)
}
