package pt.isel.pdm.battleships.ui.screens.gameplay.configuration.shipPlacing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipImage

private const val SHIP_SLOTS_CORNER_RADIUS = 10
const val SHIP_SLOTS_FACTOR = 0.6f

/**
 * A composable that represents the ship slots.
 * Contains the slots for each ship type.
 */
@Composable
fun ShipSlots(tileSize: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(SHIP_SLOTS_FACTOR)
            .fillMaxHeight(SHIP_SLOTS_FACTOR)
            .padding(end = PLACING_MENU_PADDING.dp)
            .clip(RoundedCornerShape(SHIP_SLOTS_CORNER_RADIUS.dp))
            .background(Color.LightGray)
    ) {
        Row {
            ShipSlot(ShipType.BATTLESHIP, tileSize)
            ShipSlot(ShipType.CARRIER, tileSize)
            ShipSlot(ShipType.CRUISER, tileSize)
            ShipSlot(ShipType.DESTROYER, tileSize)
            ShipSlot(ShipType.SUBMARINE, tileSize)
        }
    }
}

@Composable
fun ShipSlot(type: ShipType, tileSize: Float) {
    // TODO: put the "ship sillouette" / empty slot image
    // ShipImage(type = type, orientation = Orientation.VERTICAL)
}
