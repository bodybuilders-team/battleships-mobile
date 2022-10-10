package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.SHIP_VIEW_BOX_HEIGHT_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView

private const val SHIP_SLOTS_CORNER_RADIUS = 10
const val SHIP_SLOTS_FACTOR = 0.6f

/**
 * A composable that represents the ship slots.
 * Contains the slots for each ship type.
 *
 * @param shipTypes the list of ship types to be presented
 * @param tileSize the size of the tiles in the board
 */
@Composable
fun ShipSlotsView(shipTypes: List<ShipType>, tileSize: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(SHIP_SLOTS_FACTOR)
            .fillMaxHeight(SHIP_SLOTS_FACTOR)
            .padding(end = PLACING_MENU_PADDING.dp)
            .clip(RoundedCornerShape(SHIP_SLOTS_CORNER_RADIUS.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        LazyRow {
            items(ShipType.values()) { ship ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val shipTypeCount = shipTypes.count { it == ship }

                    // TODO: put the "ship silhouette" / empty slot image when the ship is out the slot
                    Box(
                        modifier = Modifier
                            .height((tileSize * SHIP_VIEW_BOX_HEIGHT_FACTOR).dp)
                            .width(tileSize.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ShipView(
                            type = ship,
                            orientation = Orientation.VERTICAL,
                            tileSize = tileSize
                        )
                    }

                    Text(text = shipTypeCount.toString())
                }
            }
        }
    }
}

@Composable
private fun ShipSlotView(type: ShipType, tileSize: Float) {
    // TODO: put the "ship sillouette" / empty slot image
    // ShipImage(type = type, orientation = Orientation.VERTICAL)
}
