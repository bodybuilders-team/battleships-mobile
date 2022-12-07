package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.shared.components.GoBackButton
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton
import pt.isel.pdm.battleships.ui.screens.shared.components.ScreenTitle

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

/**
 * Gameplay menu screen.
 *
 * @param onMatchmakeClick the callback to be invoked when the matchmake button is clicked
 * @param onCreateGameClick the callback to be invoked when the create game button is clicked
 * @param onLobbyClick the callback to be invoked when the lobby button is clicked
 * @param onBackButtonClick the callback to be invoked when the back button is clicked
 */
@Composable
fun GameplayMenuScreen(
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
            ScreenTitle(title = stringResource(R.string.gameplayMenu_title))

            IconButton(
                onClick = onMatchmakeClick,
                painter = painterResource(R.drawable.ic_round_play_arrow_24),
                contentDescription = stringResource(R.string.gameplayMenu_quickPlayButton_description),
                text = stringResource(R.string.gameplayMenu_quickPlayButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onCreateGameClick,
                painter = painterResource(R.drawable.ic_round_add_24),
                contentDescription = stringResource(R.string.gameplayMenu_newGameButton_description),
                text = stringResource(R.string.gameplayMenu_newGameButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onLobbyClick,
                painter = painterResource(R.drawable.ic_round_search_24),
                contentDescription = stringResource(R.string.gameplayMenu_searchGameButton_description),
                text = stringResource(R.string.gameplayMenu_searchGameButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            GoBackButton(onClick = onBackButtonClick)
        }
    }
}

@Preview
@Composable
private fun GameplayMenuScreenPreview() {
    GameplayMenuScreen(
        onMatchmakeClick = {},
        onCreateGameClick = {},
        onLobbyClick = {},
        onBackButtonClick = {}
    )
}
