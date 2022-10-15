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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
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
fun ShipSlotsView(
    shipTypes: List<ShipType>,
    tileSize: Float,
    dragging: (ShipType) -> Boolean,
    onDragStart: (Ship, Offset) -> Unit,
    onDragEnd: (Ship) -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (Offset) -> Unit
) {
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
            items(ShipType.values()) { shipType ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val draggingShip = dragging(shipType)

                    val shipTypeCount = shipTypes.count { it == shipType }

                    Box(
                        modifier = Modifier
                            .height((tileSize * SHIP_VIEW_BOX_HEIGHT_FACTOR).dp)
                            .width(tileSize.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (shipTypeCount == 0) {
                            ShipView(
                                type = shipType,
                                orientation = Orientation.VERTICAL,
                                tileSize = tileSize,
                                modifier = Modifier.alpha(0.5f)
                            )
                        } else {
                            DraggableShipView(
                                ship = Ship(
                                    type = shipType,
                                    coordinate = Coordinate('A', 1),
                                    orientation = Orientation.VERTICAL
                                ),
                                tileSize = tileSize,
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragCancel = onDragCancel,
                                onDrag = onDrag,
                                modifier = Modifier.alpha(
                                    if (draggingShip && shipTypeCount == 1) 0.5f else 1f
                                ),
                                onTap = {}
                            )
                        }
                    }

                    Text(
                        text = (shipTypeCount - if (draggingShip && shipTypeCount != 0) 1 else 0)
                            .toString()
                    )
                }
            }
        }
    }
}
