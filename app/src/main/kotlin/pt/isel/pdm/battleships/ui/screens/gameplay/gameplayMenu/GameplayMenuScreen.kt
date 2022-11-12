package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuLoadingState
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.IconButton
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

/**
 * Gameplay menu screen.
 *
 * @param loadingState the current loading state of the menu
 * @param onMatchmakeClick the callback to be invoked when the matchmake button is clicked
 * @param onCreateGameClick the callback to be invoked when the create game button is clicked
 * @param onLobbyClick the callback to be invoked when the lobby button is clicked
 * @param onBackButtonClick the callback to be invoked when the back button is clicked
 */
@Composable
fun GameplayMenuScreen(
    loadingState: GameplayMenuLoadingState,
    onMatchmakeClick: () -> Unit,
    onCreateGameClick: () -> Unit,
    onLobbyClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(R.string.gameplay_menu_title))

            IconButton(
                onClick = onMatchmakeClick,
                enabled = loadingState == GameplayMenuLoadingState.NOT_LOADING,
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_play_arrow_24),
                contentDescription = stringResource(
                    R.string.gameplay_quick_play_button_description
                ),
                text = stringResource(id = R.string.gameplay_quick_play_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onCreateGameClick,
                enabled = loadingState == GameplayMenuLoadingState.NOT_LOADING,
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
                contentDescription = stringResource(R.string.gameplay_new_game_button_description),
                text = stringResource(id = R.string.gameplay_new_game_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onLobbyClick,
                enabled = loadingState == GameplayMenuLoadingState.NOT_LOADING,
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_search_24),
                contentDescription = stringResource(
                    R.string.gameplay_search_game_button_description
                ),
                text = stringResource(id = R.string.gameplay_search_game_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            GoBackButton(onClick = onBackButtonClick)
        }
    }
}
