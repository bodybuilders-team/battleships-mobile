package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.GameConfigSelector
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.IntSelector
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.ShipSelector
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.IconButton
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

private const val MIN_SHOTS_PER_TURN = 1
private const val MAX_SHOTS_PER_TURN = 5
private const val DEFAULT_SHOTS_PER_TURN = 1

private const val MIN_TIME_PER_TURN = 10 // Seconds
private const val MAX_TIME_PER_TURN = 120 // Seconds
private const val DEFAULT_TIME_PER_TURN = 30 // Seconds

private const val MIN_TIME_FOR_BOARD_CONFIG = 10 // Seconds
private const val MAX_TIME_FOR_BOARD_CONFIG = 120 // Seconds
private const val DEFAULT_TIME_FOR_LAYOUT_PHASE = 60 // Seconds

const val NAME_TEXT_FIELD_HORIZONTAL_PADDING = 32
const val NAME_TEXT_FIELD_BOTTOM_PADDING = 10
private const val MAX_NAME_LENGTH = 40

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param state the current state of the screen
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 * @param onBackButtonClicked Callback that is called when the user clicks the back button
 */
@Composable
fun GameConfigurationScreen(
    state: GameConfigurationState,
    onGameConfigured: (gameName: String, gameConfig: GameConfig) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    var gameName by remember { mutableStateOf("") }
    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var shotsPerTurn by remember { mutableStateOf(DEFAULT_SHOTS_PER_TURN) }
    var timePerTurn by remember { mutableStateOf(DEFAULT_TIME_PER_TURN) }
    var timeForLayoutPhase by remember { mutableStateOf(DEFAULT_TIME_FOR_LAYOUT_PHASE) }
    val ships =
        remember { mutableStateMapOf<ShipType, Int>() }.also { it.putAll(ShipType.defaultsMap) }

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
                boardSize = boardSize,
                shotsPerTurn = shotsPerTurn,
                timePerTurn = timePerTurn,
                timeForLayoutPhase = timeForLayoutPhase,
                ships = ships,
                onGameConfigured = onGameConfigured
            )

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}

@Composable
private fun GameNameTextFieldView(
    gameName: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        label = {
            Text(text = stringResource(R.string.gameConfig_gameNameTextField_label))
        },
        value = gameName,
        onValueChange = {
            if (it.length <= MAX_NAME_LENGTH)
                onValueChange(it.trim())
        },
        placeholder = { Text(text = stringResource(R.string.gameConfig_gameNameTextField_placeholder)) },
        singleLine = true,
        modifier = Modifier
            .padding(horizontal = NAME_TEXT_FIELD_HORIZONTAL_PADDING.dp)
            .padding(bottom = NAME_TEXT_FIELD_BOTTOM_PADDING.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun GridSizeSelector(boardSize: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = boardSize,
        valueRange = Board.MIN_BOARD_SIZE..Board.MAX_BOARD_SIZE,
        label = stringResource(R.string.gameConfig_gridSize_text),
        valueLabel = { "$it x $it" },
        onValueChange = onValueChange
    )
}

@Composable
fun BoardConfigurationTimeSelector(timeForLayoutPhase: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = timeForLayoutPhase,
        valueRange = MIN_TIME_FOR_BOARD_CONFIG..MAX_TIME_FOR_BOARD_CONFIG,
        label = stringResource(R.string.gameConfig_timeForGridLayout_text),
        valueLabel = { "$it s" },
        onValueChange = onValueChange
    )
}

@Composable
private fun ShotsPerTurnSelector(shotsPerTurn: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = shotsPerTurn,
        valueRange = MIN_SHOTS_PER_TURN..MAX_SHOTS_PER_TURN,
        label = stringResource(R.string.gameConfig_shotsPerTurn_text),
        onValueChange = onValueChange
    )
}

@Composable
private fun TimePerTurnSelector(timePerTurn: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = timePerTurn,
        valueRange = MIN_TIME_PER_TURN..MAX_TIME_PER_TURN,
        label = stringResource(R.string.gameConfig_timePerTurn_text),
        valueLabel = { "$it s" },
        onValueChange = onValueChange
    )
}

@Composable
private fun GameConfigShipSelector(
    ships: Map<ShipType, Int>,
    boardSize: Int,
    onShipAdded: (ShipType) -> Unit,
    onShipRemoved: (ShipType) -> Unit
) {
    GameConfigSelector(leftSideContent = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.gameConfig_ships_label),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { },
                imageVector = ImageVector.vectorResource(R.drawable.ic_boat_24),
                contentDescription = stringResource(R.string.gameConfig_manageShipsButton_iconDescription),
                text = stringResource(R.string.gameConfig_manageShipsButton_text)
            )
        }
    }, rightSideContent = {
            ShipSelector(
                shipTypes = ships,
                boardSize = boardSize,
                onShipAdded = onShipAdded,
                onShipRemoved = onShipRemoved
            )
        })
}

@Composable
private fun CreateGameButton(
    state: GameConfigurationState,
    gameName: String,
    boardSize: Int,
    shotsPerTurn: Int,
    timePerTurn: Int,
    timeForLayoutPhase: Int,
    ships: Map<ShipType, Int>,
    onGameConfigured: (gameName: String, gameConfig: GameConfig) -> Unit
) {
    IconButton(
        enabled = state == LINKS_LOADED,
        onClick = {
            onGameConfigured(
                gameName.ifEmpty { "Game" },
                GameConfig(
                    gridSize = boardSize,
                    shotsPerTurn = shotsPerTurn,
                    maxTimePerRound = timePerTurn,
                    maxTimeForLayoutPhase = timeForLayoutPhase,
                    ships = ships
                )
            )
        },
        imageVector = ImageVector.vectorResource(R.drawable.ic_round_add_24),
        contentDescription = stringResource(R.string.gameConfig_createGameButton_iconDescription),
        text = stringResource(R.string.gameConfig_createGameButton_text),
        modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
    )
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
