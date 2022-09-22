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
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board.Companion.DEFAULT_BOARD_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.configuration.BoardConfiguration
import pt.isel.pdm.battleships.ui.screens.gameplay.configuration.GridSizeSelector

private const val GAME_CONFIG_TITLE_PADDING = 8

/**
 * Screen that allows the user to configure a new game before starting it.
 *
 * @param onBackButtonPressed what to do when the back button is pressed
 */
@Composable
fun NewGame(onBackButtonPressed: () -> Unit) {
    var boardSize by remember { mutableStateOf(DEFAULT_BOARD_SIZE) }
    var configureBoard by remember { mutableStateOf(false) }

    Column {
        when (configureBoard) {
            true -> BoardConfiguration(boardSize) {
                configureBoard = false
            }
            false -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.game_config_title),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(GAME_CONFIG_TITLE_PADDING.dp)
                )

                GridSizeSelector(boardSize) { boardSize = it }

                Button(onClick = { configureBoard = true }) {
                    Text(stringResource(id = R.string.game_config_configure_board_button_text))
                }

                Button(onClick = onBackButtonPressed) {
                    Text(text = stringResource(id = R.string.back_button_text))
                }
            }
        }
    }
}
