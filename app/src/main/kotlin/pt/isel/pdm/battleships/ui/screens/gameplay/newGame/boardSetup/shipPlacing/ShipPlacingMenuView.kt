package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.utils.IconButton

const val PLACING_MENU_PADDING = 6

/**
 * A composable that represents the ship placing menu.
 * It contains the ship slots and some buttons.
 *
 * @param shipTypes the list of ship types to be presented
 * @param tileSize the size of the tiles in the board
 * @param onRandomBoardButtonPressed the callback to be invoked when the user presses the random board button
 * @param onConfirmBoardButtonPressed the callback to be invoked when the user presses the confirm board button
 */
@Composable
fun ShipPlacingMenuView(
    shipTypes: List<ShipType>,
    tileSize: Float,
    dragging: (ShipType) -> Boolean,
    onDragStart: (Ship, Offset) -> Unit,
    onDragEnd: (Ship) -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (Offset) -> Unit,
    onRandomBoardButtonPressed: () -> Unit,
    onConfirmBoardButtonPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = PLACING_MENU_PADDING.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ShipSlotsView(
            shipTypes = shipTypes,
            tileSize = tileSize,
            dragging = dragging,
            onDragStart = onDragStart,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            onDrag = onDrag
        )

        // Buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onRandomBoardButtonPressed,
                imageVector = ImageVector.vectorResource(R.drawable.ic_round_cycle_24),
                contentDescription = stringResource(id = R.string.gameplay_random_board_button_description),
                text = stringResource(id = R.string.gameplay_random_board_button_text)
            )
            IconButton(
                onClick = onConfirmBoardButtonPressed,
                imageVector = ImageVector.vectorResource(R.drawable.ic_round_check_24),
                contentDescription = stringResource(id = R.string.game_config_confirm_board_button_description),
                text = stringResource(id = R.string.game_config_confirm_board_button_text)
            )
        }
    }
}
