package pt.isel.pdm.battleships.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.aboutDevelopers.AboutDevelopers
import pt.isel.pdm.battleships.ui.screens.gameplay.Gameplay
import pt.isel.pdm.battleships.ui.screens.login.Login
import pt.isel.pdm.battleships.ui.screens.ranking.Ranking

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

private const val LOGO_MAX_SIZE_FACTOR = 0.6f

/**
 * The main menu of the application.
 */
@Composable
fun MainMenu() {
    val currentPage = remember { mutableStateOf(Page.MAIN_MENU) }

    val playButtonText = stringResource(id = R.string.main_menu_play_button_text)
    val loginButtonText = stringResource(id = R.string.main_menu_login_button_text)
    val rankingButtonText = stringResource(id = R.string.main_menu_ranking_button_text)
    val aboutDevsButtonText = stringResource(id = R.string.main_menu_about_devs_button_text)

    Box {
        when (currentPage.value) {
            Page.MAIN_MENU -> Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Battleships Logo",
                    modifier = Modifier.fillMaxSize(LOGO_MAX_SIZE_FACTOR)
                )

                Button(onClick = { currentPage.value = Page.GAMEPLAY }) {
                    Text(text = playButtonText)
                }
                Button(onClick = { currentPage.value = Page.LOGIN }) {
                    Text(text = loginButtonText)
                }
                Button(onClick = { currentPage.value = Page.RANKING }) {
                    Text(text = rankingButtonText)
                }
                Button(onClick = { currentPage.value = Page.ABOUT_DEVS }) {
                    Text(text = aboutDevsButtonText)
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
}
