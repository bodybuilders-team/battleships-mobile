package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

/**
 * Represents the available pages in the application.
 */
private enum class Page {
    MAIN_MENU,
    GAMEPLAY,
    LOGIN,
    RANKING,
    ABOUT_DEVS
}


/**
 * The main menu of the application.
 */
@Composable
fun MainMenu() {
    val currentPage = remember { mutableStateOf(Page.MAIN_MENU) }

    /*Image(
        painter = painterResource(
            R.drawable.logo
        ),
        contentDescription = "Battleships Logo"
    )*/

    val playButtonText = stringResource(id = R.string.main_menu_play_button_text)
    val loginButtonText = stringResource(id = R.string.main_menu_login_button_text)
    val rankingButtonText = stringResource(id = R.string.main_menu_ranking_button_text)
    val aboutDevsButtonText = stringResource(id = R.string.main_menu_about_devs_button_text)

    when (currentPage.value) {
        Page.MAIN_MENU -> Column {
            Button(onClick = { currentPage.value = Page.GAMEPLAY }) {
                Text(text = playButtonText, color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.LOGIN }) {
                Text(text = loginButtonText, color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.RANKING }) {
                Text(text = rankingButtonText, color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.ABOUT_DEVS }) {
                Text(text = aboutDevsButtonText, color = Color.Black)
            }
        }
        Page.GAMEPLAY -> Gameplay {
            currentPage.value = Page.MAIN_MENU
        }
        Page.LOGIN -> Login {
            currentPage.value = Page.MAIN_MENU
        }
        Page.RANKING -> Ranking {
            currentPage.value = Page.MAIN_MENU
        }
        Page.ABOUT_DEVS -> AboutDevelopers {
            currentPage.value = Page.MAIN_MENU
        }
    }
}
