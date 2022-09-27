package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R

private enum class Page {
    MENU,
    NEW_GAME,
    SEARCH_GAME
}

/**
 * The gameplay screen.
 *
 * @param onBackToMenuButtonPress what to do when the "Back to menu" button is pressed
 */
@Composable
fun Gameplay(onBackToMenuButtonPress: () -> Unit) {
    var currentPage by remember { mutableStateOf(Page.MENU) }

    when (currentPage) {
        Page.MENU -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { currentPage = Page.NEW_GAME }) {
                Text(text = stringResource(id = R.string.gameplay_new_game_button_text))
            }

            Button(onClick = { currentPage = Page.SEARCH_GAME }) {
                Text(text = stringResource(id = R.string.gameplay_search_game_button_text))
            }

            Button(onClick = onBackToMenuButtonPress) {
                Text(text = stringResource(id = R.string.back_to_menu_button_text))
            }
        }

        Page.NEW_GAME -> NewGame(onBackButtonPressed = {
            currentPage = Page.MENU
        })

        Page.SEARCH_GAME -> SearchGame() /*{
            currentPage = Page.MENU
        }*/
    }
}
