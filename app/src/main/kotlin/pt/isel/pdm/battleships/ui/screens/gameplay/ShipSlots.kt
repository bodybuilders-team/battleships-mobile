package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TILE_SIZE

private const val SHIP_SLOTS_CORNER_RADIUS = 10
private const val SHIP_SLOTS_FACTOR = 0.6f
const val SHIP_SLOTS_WIDTH = (Board.BOARD_SIDE_LENGTH + 1) * TILE_SIZE * SHIP_SLOTS_FACTOR
const val SHIP_SLOTS_HEIGHT = (Board.BOARD_SIDE_LENGTH + 1) * TILE_SIZE * SHIP_SLOTS_FACTOR

/**
 * A composable that represents the ship slots.
 * Contains the slots for each ship type.
 */
@Composable
fun ShipSlots() {
    Box(
        modifier = Modifier
            .fillMaxWidth(SHIP_SLOTS_FACTOR)
            .fillMaxHeight(SHIP_SLOTS_FACTOR)
            .padding(end = PLACING_MENU_PADDING.dp)
            .clip(RoundedCornerShape(SHIP_SLOTS_CORNER_RADIUS.dp))
            .background(Color.LightGray)
    )
}
