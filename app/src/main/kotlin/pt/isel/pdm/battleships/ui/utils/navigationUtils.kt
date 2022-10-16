package pt.isel.pdm.battleships.ui.utils

import android.content.Context
import android.content.Intent

/**
 * Navigates to the specified activity.
 *
 */
inline fun <reified T> Context.navigateTo(
    beforeNavigation: (Intent) -> Unit = {}
) {
    val intent = Intent(this, T::class.java)

    beforeNavigation(intent)

    startActivity(intent)
}
