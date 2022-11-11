package pt.isel.pdm.battleships.utils // ktlint-disable filename

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Navigates to the specified activity.
 *
 * @param clazz the class of the activity to navigate to
 * @param beforeNavigation a function that is called before the navigation is performed
 */
fun <T> Context.navigateTo(
    clazz: Class<T>,
    beforeNavigation: (Intent) -> Unit = {}
) {
    val intent = Intent(this, clazz)

    beforeNavigation(intent)
    startActivity(intent)
}

/**
 * Navigates to the specified activity.
 *
 * @param beforeNavigation a function that is called before the navigation is performed
 */
inline fun <reified T> Context.navigateTo(
    noinline beforeNavigation: (Intent) -> Unit = {}
) {
    navigateTo(T::class.java, beforeNavigation)
}

/**
 * Navigates to the specified activity and waits for a result.
 *
 * @param clazz the class of the activity to navigate to
 * @param beforeNavigation a function that is called before the navigation is performed
 */
fun <T> ComponentActivity.navigateToForResult(
    activityResultLauncher: ActivityResultLauncher<Intent?>,
    clazz: Class<T>,
    beforeNavigation: (Intent) -> Unit = {}
) {
    val intent = Intent(this, clazz)

    beforeNavigation(intent)

    activityResultLauncher.launch(intent)
}
