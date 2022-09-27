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
 * @param onSelectedOrientation the callback to be invoked when the user clicks the change orientation button
 * @param onRandomBoardButtonClick the callback to be invoked when the user clicks the random board button
 */
@Composable
fun ShipPlacingMenu(
    tileSize: Float,
    onSelectedOrientation: () -> Unit,
    onRandomBoardButtonClick: () -> Unit
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
            Button(onClick = onSelectedOrientation) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.gameplay_change_orientation_button_text)
                )
            }
            Button(onClick = onRandomBoardButtonClick) {
                Text(stringResource(R.string.gameplay_random_board_button_text))
            }
        }
    }
}
