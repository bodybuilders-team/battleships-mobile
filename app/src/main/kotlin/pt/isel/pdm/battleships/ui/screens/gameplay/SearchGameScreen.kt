package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.GameConfigurationScreen
import pt.isel.pdm.battleships.viewModels.SearchGameViewModel

/**
 * Screen that displays the search game menu.
 *
 * @param vm search game view model
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun SearchGameScreen(
    vm: SearchGameViewModel,
    onBackButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        GameConfigurationScreen(
            onGameConfigured = { gameConfig ->
                vm.matchmake(gameConfig)
            },
            onBackButtonClicked = onBackButtonClicked
        )
    }
}
