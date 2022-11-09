package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.GameConfigSelector
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.IntSelector
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components.ShipSelector
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.IconButton
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

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

val DEFAULT_SHIP_TYPES = ShipType.values().toList()

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 * @param onBackButtonClicked Callback that is called when the user clicks the back button
 */
@Composable
fun GameConfigurationScreen(
    state: GameConfigurationState,
    onGameConfigured: (GameConfig) -> Unit,
    onGameCreated: () -> Unit,
    errorMessage: String?,
    onBackButtonClicked: () -> Unit

) {
    LaunchedEffect(state) {
        if (state == GameConfigurationState.GAME_CREATED) {
            onGameCreated()
        }
    }

    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        if (errorMessage == null) {
            return@LaunchedEffect
        }

        Toast.makeText(
            context,
            errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var shotsPerTurn by remember { mutableStateOf(DEFAULT_SHOTS_PER_TURN) }
    var timePerTurn by remember { mutableStateOf(DEFAULT_TIME_PER_TURN) }
    var timeForBoardConfig by remember { mutableStateOf(DEFAULT_TIME_FOR_BOARD_CONFIG) }
    var ships by remember { mutableStateOf(DEFAULT_SHIP_TYPES) }

    BattleshipsScreen {
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
            GameConfigSelector(leftSideContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.game_config_ships_text),
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { },
                        imageVector = ImageVector.vectorResource(
                            R.drawable.ic_round_directions_boat_24
                        ),
                        contentDescription = "",
                        text = "Manage ships"
                    )
                }
            }, rightSideContent = {
                    ShipSelector(
                        shipTypes = ships,
                        onShipAdded = { ships = ships + it },
                        onShipRemoved = { ships = ships - it }
                    )
                })

            IconButton(
                enabled = state != GameConfigurationState.CREATING_GAME,
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
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
                contentDescription = stringResource(R.string.gameplay_new_game_button_description),
                text = stringResource(id = R.string.game_config_create_game_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}
