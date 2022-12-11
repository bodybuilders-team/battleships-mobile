package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

const val PLACING_MENU_PADDING = 6
private const val BETWEEN_BUTTONS_PADDING = 6

/**
 * A composable that The ship placing menu.
 * It contains the ship slots and some buttons.
 *
 * @param shipTypes the list of ship types to be presented
 * @param draggingUnplaced the function that returns true if an unplaced ship with the specified ship
 * type is being dragged
 * @param onDragStart the function that is called when a ship starts being dragged
 * @param onDragEnd the function that is called when a ship is dropped
 * @param onDragCancel the function that is called when a ship drag is canceled
 * @param onDrag the function that is called when a ship is dragged
 * @param onRandomBoardButtonPressed the callback to be invoked when the user presses the
 * random board button
 * @param onConfirmBoardButtonPressed the callback to be invoked when the user presses the
 * confirm board button
 */
@Composable
fun ShipPlacingMenuView(
    shipTypes: Map<ShipType, Int>,
    draggingUnplaced: (ShipType) -> Boolean,
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
            draggingUnplaced = draggingUnplaced,
            onDragStart = onDragStart,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            onDrag = onDrag
        )

        // Buttons
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onRandomBoardButtonPressed,
                painter = painterResource(R.drawable.ic_round_cycle_24),
                contentDescription = stringResource(R.string.gameplay_randomBoardButton_description),
                text = stringResource(R.string.gameplay_randomBoardButton_text),
                modifier = Modifier.padding(bottom = BETWEEN_BUTTONS_PADDING.dp)
            )
            IconButton(
                onClick = onConfirmBoardButtonPressed,
                painter = painterResource(R.drawable.ic_round_check_24),
                contentDescription = stringResource(R.string.gameplay_confirmBoardButton_description),
                text = stringResource(R.string.gameplay_confirmBoardButton_text)
            )
        }
    }
}
