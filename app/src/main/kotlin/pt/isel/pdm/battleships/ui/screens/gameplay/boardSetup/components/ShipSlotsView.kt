package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components

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
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components.SHIP_VIEW_BOX_HEIGHT_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.DEFAULT_TILE_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.ShipView

private const val SHIP_SLOTS_CORNER_RADIUS = 10
private const val SHIP_SLOTS_FACTOR = 0.6f
private const val SHIP_ALPHA_WHEN_ZERO_QUANTITY = 0.5f
private const val SHIP_ALPHA_WHEN_MORE_THAN_ZERO_QUANTITY = 1f

/**
 * A composable that The ship slots.
 * Contains the slots for each ship type.
 *
 * @param shipTypes the list of ship types to be presented
 * @param draggingUnplaced the function that returns true if an unplaced ship with the specified ship
 * type is being dragged
 * @param onDragStart the function that is called when a ship starts being dragged
 * @param onDragEnd the function that is called when a ship is dropped
 * @param onDragCancel the function that is called when a ship drag is canceled
 * @param onDrag the function that is called when a ship is dragged
 */
@Composable
fun ShipSlotsView(
    shipTypes: Map<ShipType, Int>,
    draggingUnplaced: (ShipType) -> Boolean,
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
            items(shipTypes.entries.toList()) { (shipType, quantity) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val draggingUnplacedShip = draggingUnplaced(shipType)

                    Box(
                        modifier = Modifier
                            .height((DEFAULT_TILE_SIZE * SHIP_VIEW_BOX_HEIGHT_FACTOR).dp)
                            .width(DEFAULT_TILE_SIZE.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (quantity == 0) {
                            ShipView(
                                type = shipType,
                                orientation = Orientation.VERTICAL,
                                tileSize = DEFAULT_TILE_SIZE,
                                modifier = Modifier.alpha(SHIP_ALPHA_WHEN_ZERO_QUANTITY)
                            )
                        } else {
                            DraggableShipView(
                                ship = Ship(
                                    type = shipType,
                                    coordinate = Coordinate.first,
                                    orientation = Orientation.VERTICAL
                                ),
                                tileSize = DEFAULT_TILE_SIZE,
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragCancel = onDragCancel,
                                onDrag = onDrag,
                                modifier = Modifier
                                    .alpha(
                                        if (draggingUnplacedShip && quantity == 1)
                                            SHIP_ALPHA_WHEN_ZERO_QUANTITY
                                        else
                                            SHIP_ALPHA_WHEN_MORE_THAN_ZERO_QUANTITY
                                    ),
                                onTap = {}
                            )
                        }
                    }

                    Text(
                        text = (quantity - if (draggingUnplacedShip && quantity > 0) 1 else 0)
                            .toString()
                    )
                }
            }
        }
    }
}
