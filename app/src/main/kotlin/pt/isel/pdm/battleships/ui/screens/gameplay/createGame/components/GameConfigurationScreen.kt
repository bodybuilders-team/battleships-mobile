package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.shared.components.GoBackButton
import pt.isel.pdm.battleships.ui.screens.shared.components.ScreenTitle

private const val DEFAULT_SHOTS_PER_TURN = 1
private const val DEFAULT_TIME_PER_TURN = 100 // Seconds
private const val DEFAULT_TIME_FOR_LAYOUT_PHASE = 100 // Seconds

const val NAME_TEXT_FIELD_HORIZONTAL_PADDING = 32
const val NAME_TEXT_FIELD_BOTTOM_PADDING = 10

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param state the current state of the screen
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 * @param onBackButtonClicked Callback that is called when the user clicks the back button
 */
@Composable
fun GameConfigurationScreen(
    state: CreateGameState,
    onGameConfigured: (gameName: String, gameConfig: GameConfig) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    var gameName by remember { mutableStateOf("") }
    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var shotsPerTurn by remember { mutableStateOf(DEFAULT_SHOTS_PER_TURN) }
    var timePerTurn by remember { mutableStateOf(DEFAULT_TIME_PER_TURN) }
    var timeForLayoutPhase by remember { mutableStateOf(DEFAULT_TIME_FOR_LAYOUT_PHASE) }
    val ships = remember { mutableStateMapOf<ShipType, Int>() }
        .also { it.putAll(ShipType.defaultsMap) }

    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(R.string.gameConfig_title))

            GameNameTextFieldView(gameName = gameName, onValueChange = { gameName = it })
            GridSizeSelector(boardSize = boardSize, onValueChange = {
                boardSize = it
                ships.keys.forEach { shipType -> ships[shipType] = 1 }
            })
            BoardConfigurationTimeSelector(
                timeForLayoutPhase = timeForLayoutPhase,
                onValueChange = { timeForLayoutPhase = it }
            )
            ShotsPerTurnSelector(shotsPerTurn = shotsPerTurn, onValueChange = { shotsPerTurn = it })
            TimePerTurnSelector(timePerTurn = timePerTurn, onValueChange = { timePerTurn = it })
            GameConfigShipSelector(
                ships = ships,
                boardSize = boardSize,
                onShipAdded = { ships[it] = ships[it]!! + 1 },
                onShipRemoved = {
                    if (ships[it]!! > 0)
                        ships[it] = ships[it]!! - 1
                }
            )

            CreateGameButton(
                state = state,
                gameName = gameName,
                gridSize = boardSize,
                shotsPerTurn = shotsPerTurn,
                maxTimePerRound = timePerTurn,
                maxTimeForLayoutPhase = timeForLayoutPhase,
                ships = ships,
                onGameConfigured = onGameConfigured
            )

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}

@Preview
@Composable
fun GameConfigurationScreenPreview() {
    GameConfigurationScreen(
        state = LINKS_LOADED,
        onGameConfigured = { _, _ -> },
        onBackButtonClicked = { }
    )
}
