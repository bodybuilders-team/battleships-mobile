package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.configuration.IntSelector
import pt.isel.pdm.battleships.ui.utils.BackButton

private const val GAME_CONFIG_TITLE_PADDING = 8
private const val DEFAULT_SHOTS_PER_TURN = 1

private const val MIN_TIME_PER_TURN = 10 // Seconds
private const val MAX_TIME_PER_TURN = 120 // Seconds
private const val DEFAULT_TIME_PER_TURN = 30 // Seconds

private const val MIN_TIME_FOR_BOARD_CONFIG = 10 // Seconds
private const val MAX_TIME_FOR_BOARD_CONFIG = 120 // Seconds
private const val DEFAULT_TIME_FOR_BOARD_CONFIG = 60 // Seconds

private const val MIN_SHOTS_PER_TURN = 1
private const val MAX_SHOTS_PER_TURN = 5

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param navController the navigation controller to be used to navigate to the game screen
 * @param onGameConfigured Callback that is called when the user finishes configuring the game
 */
@Composable
fun NewGame(
    navController: NavController,
    onGameConfigured: (GameConfig) -> Unit
) {
    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var shotsPerTurn by remember { mutableStateOf(DEFAULT_SHOTS_PER_TURN) }
    var timePerTurn by remember { mutableStateOf(DEFAULT_TIME_PER_TURN) }
    var timeForBoardConfig by remember { mutableStateOf(DEFAULT_TIME_FOR_BOARD_CONFIG) }

    Column {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.game_config_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(GAME_CONFIG_TITLE_PADDING.dp)
            )

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

            Button(
                onClick = {
                    onGameConfigured(
                        GameConfig(
                            boardSize,
                            shotsPerTurn,
                            timePerTurn,
                            timeForBoardConfig
                        )
                    )
                }
            ) {
                Text(stringResource(id = R.string.game_config_create_game_button_text))
            }

            BackButton(navController)
        }
    }
}
