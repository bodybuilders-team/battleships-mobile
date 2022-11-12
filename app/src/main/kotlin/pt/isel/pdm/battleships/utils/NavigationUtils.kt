package pt.isel.pdm.battleships.utils // ktlint-disable filename

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

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
 * Navigates to the specified activity that returns a result to [activityResultLauncher]'s callback.
 *
 * @param clazz the class of the activity to navigate to
 * @param beforeNavigation a function that is called before the navigation is performed
 */
fun <T> Context.navigateToForResult(
    activityResultLauncher: ActivityResultLauncher<Intent?>,
    clazz: Class<T>,
    beforeNavigation: (Intent) -> Unit = {}
) {
    val intent = Intent(this, clazz)

    beforeNavigation(intent)

    activityResultLauncher.launch(intent)
}

/**
 * Navigates to the specified activity, with the given link keys.
 *
 * @param clazz the class of the activity to navigate to
 * @param links the link keys to set before navigating
 */
fun Context.navigateWithLinksTo(
    clazz: Class<*>,
    links: Map<String, String>? = null
) {
    navigateTo(clazz) { intent ->
        if (links == null) return@navigateTo

        intent.putExtra(Links.LINKS_KEY, Links(links))
    }
}

/**
 * Navigates to the specified activity that returns a result to [activityResultLauncher]'s callback,
 * with the given link keys.
 *
 * @param clazz the class of the activity to navigate to
 * @param links the link keys to set before navigating
 */
fun Context.navigateWithLinksToForResult(
    activityResultLauncher: ActivityResultLauncher<Intent?>,
    clazz: Class<*>,
    links: Map<String, String>? = null
) {
    navigateToForResult(activityResultLauncher, clazz, beforeNavigation = { intent ->
        if (links == null) return@navigateToForResult

        intent.putExtra(Links.LINKS_KEY, Links(links))
    })
}
