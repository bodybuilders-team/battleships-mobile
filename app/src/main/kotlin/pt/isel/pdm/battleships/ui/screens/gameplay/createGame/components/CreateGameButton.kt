package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f
private const val BUTTON_TOP_PADDING = 20

/**
 * Button that allows the user to create a game with the current configuration.
 *
 * @param state the state of the button
 * @param gameName the name of the game
 * @param gridSize the size of the board
 * @param shotsPerTurn the number of shots that each player can do per turn
 * @param maxTimePerRound the time that each player has to do their turn
 * @param maxTimeForLayoutPhase the time that the players have to configure their boards
 * @param ships the number of ships of each type
 * @param onGameConfigured callback that is called when the user creates the game
 */
@Composable
fun CreateGameButton(
    state: CreateGameViewModel.CreateGameState,
    gameName: String,
    gridSize: Int,
    shotsPerTurn: Int,
    maxTimePerRound: Int,
    maxTimeForLayoutPhase: Int,
    ships: Map<ShipType, Int>,
    onGameConfigured: (gameName: String, gameConfig: GameConfig) -> Unit
) {
    IconButton(
        enabled = state == CreateGameViewModel.CreateGameState.LINKS_LOADED,
        onClick = {
            onGameConfigured(
                gameName.ifEmpty { "Game" },
                GameConfig(
                    gridSize = gridSize,
                    shotsPerRound = shotsPerTurn,
                    maxTimePerRound = maxTimePerRound,
                    maxTimeForLayoutPhase = maxTimeForLayoutPhase,
                    ships = ships
                )
            )
        },
        painter = painterResource(R.drawable.ic_round_add_24),
        contentDescription = stringResource(R.string.gameConfig_createGameButton_iconDescription),
        text = stringResource(R.string.gameConfig_createGameButton_text),
        modifier = Modifier
            .fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            .padding(top = BUTTON_TOP_PADDING.dp)
    )
}
