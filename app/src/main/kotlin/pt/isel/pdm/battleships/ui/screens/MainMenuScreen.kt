package pt.isel.pdm.battleships.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R

private const val LOGO_MAX_SIZE_FACTOR = 0.6f

/**
 * The main menu of the application.
 *
 * @param navController the navigation controller
 */
@Composable
fun MainMenuScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_content_description),
            modifier = Modifier.fillMaxSize(LOGO_MAX_SIZE_FACTOR)
        )

        Button(onClick = { navController.navigate("gameplay") }) {
            Text(text = stringResource(id = R.string.main_menu_play_button_text))
        }
        Button(onClick = { navController.navigate("login") }) {
            Text(text = stringResource(id = R.string.main_menu_login_button_text))
        }
        Button(onClick = { navController.navigate("ranking") }) {
            Text(text = stringResource(id = R.string.main_menu_ranking_button_text))
        }
        Button(onClick = { navController.navigate("about") }) {
            Text(text = stringResource(id = R.string.main_menu_about_button_text))
        }
    }
}
