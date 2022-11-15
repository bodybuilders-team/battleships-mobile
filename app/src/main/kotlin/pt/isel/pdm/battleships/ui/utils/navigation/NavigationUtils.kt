package pt.isel.pdm.battleships.ui.utils.navigation

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
 * @param activityResultLauncher the activity result launcher to use
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
 * @param links the links to set before navigating
 */
fun Context.navigateWithLinksTo(
    clazz: Class<*>,
    links: Links
) {
    navigateTo(clazz) { intent ->
        intent.putExtra(Links.LINKS_KEY, links)
    }
}

/**
 * Navigates to the specified activity, with the given link keys.
 *
 * @param links the links to set before navigating
 */
inline fun <reified T> Context.navigateWithLinksTo(
    links: Links
) {
    navigateTo<T> { intent ->
        intent.putExtra(Links.LINKS_KEY, links)
    }
}

/**
 * Navigates to the specified activity that returns a result to [activityResultLauncher]'s callback,
 * with the given link keys.
 *
 * @param activityResultLauncher the activity result launcher to use
 * @param clazz the class of the activity to navigate to
 * @param links the links to set before navigating
 */
fun Context.navigateWithLinksToForResult(
    activityResultLauncher: ActivityResultLauncher<Intent?>,
    clazz: Class<*>,
    links: Links
) {
    navigateToForResult(activityResultLauncher, clazz, beforeNavigation = { intent ->
        intent.putExtra(Links.LINKS_KEY, links)
    })
}
