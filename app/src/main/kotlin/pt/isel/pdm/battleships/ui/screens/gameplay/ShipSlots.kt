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

private const val SHIP_SLOTS_CORNER_RADIUS = 10
const val SHIP_SLOTS_FACTOR = 0.6f

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
