package pt.isel.pdm.battleships.ui.screens.gameplay.createGame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.OPPONENT_FOUND
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components.GameConfigurationScreen
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner

/**
 * CreateGame screen.
 *
 * @param state the current state of the screen
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 * @param onBackButtonClicked Callback that is called when the user clicks the back button
 */
@Composable
fun CreateGameScreen(
    state: CreateGameViewModel.CreateGameState,
    onGameConfigured: (gameName: String, gameConfig: GameConfig) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when (state) {
                WAITING_FOR_OPPONENT, OPPONENT_FOUND -> LoadingSpinner("Waiting for opponent...")
                else -> GameConfigurationScreen(state, onGameConfigured, onBackButtonClicked)
            }
        }
    }
}
