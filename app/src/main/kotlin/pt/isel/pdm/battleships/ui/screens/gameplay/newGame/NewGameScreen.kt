package pt.isel.pdm.battleships.ui.screens.gameplay.newGame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.MenuButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

private const val MIN_SHOTS_PER_TURN = 1
private const val MAX_SHOTS_PER_TURN = 5
private const val DEFAULT_SHOTS_PER_TURN = 1

private const val MIN_TIME_PER_TURN = 10 // Seconds
private const val MAX_TIME_PER_TURN = 120 // Seconds
private const val DEFAULT_TIME_PER_TURN = 30 // Seconds

private const val MIN_TIME_FOR_BOARD_CONFIG = 10 // Seconds
private const val MAX_TIME_FOR_BOARD_CONFIG = 120 // Seconds
private const val DEFAULT_TIME_FOR_BOARD_CONFIG = 60 // Seconds

val DEFAULT_SHIP_TYPES = ShipType.values().toList()

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param navController the navigation controller to be used to navigate to the game screen
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 */
@Composable
fun NewGameScreen(
    navController: NavController,
    onGameConfigured: (GameConfig) -> Unit
) {
    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var shotsPerTurn by remember { mutableStateOf(DEFAULT_SHOTS_PER_TURN) }
    var timePerTurn by remember { mutableStateOf(DEFAULT_TIME_PER_TURN) }
    var timeForBoardConfig by remember { mutableStateOf(DEFAULT_TIME_FOR_BOARD_CONFIG) }
    var ships by remember { mutableStateOf(DEFAULT_SHIP_TYPES) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.game_config_title))

        // Grid Size Selector
        IntSelector(
            defaultValue = boardSize,
            valueRange = Board.MIN_BOARD_SIZE..Board.MAX_BOARD_SIZE,
            label = stringResource(R.string.game_config_grid_size_text),
            valueLabel = { "$it x $it" },
            onValueChange = { boardSize = it }
        )

        // Board Configuration Time
        IntSelector(
            defaultValue = timeForBoardConfig,
            valueRange = MIN_TIME_FOR_BOARD_CONFIG..MAX_TIME_FOR_BOARD_CONFIG,
            label = stringResource(R.string.game_config_time_for_board_config_text),
            valueLabel = { "$it s" },
            onValueChange = { timeForBoardConfig = it }
        )

        // Shots Per Turn Selector
        IntSelector(
            defaultValue = shotsPerTurn,
            valueRange = MIN_SHOTS_PER_TURN..MAX_SHOTS_PER_TURN,
            label = stringResource(R.string.game_config_shots_per_turn_text),
            onValueChange = { shotsPerTurn = it }
        )

        // Time Per Turn Selector
        IntSelector(
            defaultValue = timePerTurn,
            valueRange = MIN_TIME_PER_TURN..MAX_TIME_PER_TURN,
            label = stringResource(R.string.game_config_time_per_turn_text),
            valueLabel = { "$it s" },
            onValueChange = { timePerTurn = it }
        )

        // Ship Selector
        // TODO limitation on number of ships based on board size (type of ship also matters)
        //  50% of the board max?
        GameConfigSelector(
            leftSideContent = {
                Text(
                    text = stringResource(R.string.game_config_ships_text),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            },
            rightSideContent = {
                ShipSelector(
                    shipTypes = ships,
                    onShipAdded = { ships = ships + it },
                    onShipRemoved = { ships = ships - it }
                )
            }
        )

        MenuButton(
            onClick = {
                onGameConfigured(
                    GameConfig(
                        gridSize = boardSize,
                        shotsPerTurn = shotsPerTurn,
                        maxTimePerShot = timePerTurn,
                        maxTimeForLayoutPhase = timeForBoardConfig,
                        ships = ships
                    )
                )
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
            iconDescription = stringResource(R.string.gameplay_new_game_button_description),
            text = stringResource(id = R.string.game_config_create_game_button_text)
        )

        GoBackButton(navController)
    }
}
