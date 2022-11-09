package pt.isel.pdm.battleships.utils // ktlint-disable filename

import android.content.Context
import android.content.Intent

/**
 * Navigates to the specified activity.
 *
 * @param beforeNavigation a function that is called before the navigation is performed
 */
inline fun <reified T> Context.navigateTo(
    beforeNavigation: (Intent) -> Unit = {}
) {
    val intent = Intent(this, T::class.java)

    beforeNavigation(intent)
    startActivity(intent)
}
