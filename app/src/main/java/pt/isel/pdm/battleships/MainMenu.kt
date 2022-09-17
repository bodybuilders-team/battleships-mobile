package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private enum class Page {
    MAIN_MENU,
    GAMEPLAY,
    LOGIN,
    RANKING,
    ABOUT_DEVS
}

@Composable
fun MainMenu() {
    val currentPage = remember { mutableStateOf(Page.MAIN_MENU) }

    when (currentPage.value) {
        Page.MAIN_MENU -> Column {
            Button(onClick = { currentPage.value = Page.GAMEPLAY }) {
                Text(text = "Play", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.LOGIN }) {
                Text(text = "Login", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.RANKING }) {
                Text(text = "Ranking", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.ABOUT_DEVS }) {
                Text(text = "About the developers", color = Color.Black)
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
        Page.ABOUT_DEVS -> AboutDevelopers(
            backToMenuCallback = {
                currentPage.value = Page.MAIN_MENU
            }
        )
    }
}
