package pt.isel.pdm.battleships.ui.screens.gameplay.configuration.shipPlacing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

const val PLACING_MENU_PADDING = 6

/**
 * A composable that represents the ship placing menu.
 * It contains the ship slots and some buttons.
 *
 * @param onChangeOrientationButtonPressed the callback to be invoked when the user presses the change orientation button
 * @param onRandomBoardButtonPressed the callback to be invoked when the user presses the random board button
 * @param onConfirmBoardButtonPressed the callback to be invoked when the user presses the confirm board button
 */
@Composable
fun ShipPlacingMenu(
    tileSize: Float,
    onChangeOrientationButtonPressed: () -> Unit,
    onRandomBoardButtonPressed: () -> Unit,
    onConfirmBoardButtonPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = PLACING_MENU_PADDING.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ShipSlots(tileSize)

        // Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onChangeOrientationButtonPressed) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.gameplay_change_orientation_button_text)
                )
            }
            Button(onClick = onRandomBoardButtonPressed) {
                Text(stringResource(R.string.gameplay_random_board_button_text))
            }
            Button(onClick = onConfirmBoardButtonPressed) {
                Text(stringResource(R.string.game_config_confirm_board_button_text))
            }
        }
    }
}
