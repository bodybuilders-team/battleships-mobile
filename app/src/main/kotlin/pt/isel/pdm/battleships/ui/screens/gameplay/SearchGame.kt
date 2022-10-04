package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import pt.isel.pdm.battleships.ui.utils.BackButton

/**
 * Screen that displays the search game menu.
 *
 * @param navController the navigation controller used to navigate between screens
 */
@Composable
fun SearchGame(navController: NavController) {
    BackButton(navController)
}
