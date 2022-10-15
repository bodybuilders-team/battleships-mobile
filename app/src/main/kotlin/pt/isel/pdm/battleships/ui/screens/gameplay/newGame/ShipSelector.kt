package pt.isel.pdm.battleships.ui.screens.gameplay.newGame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.board.DEFAULT_TILE_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView
import pt.isel.pdm.battleships.ui.utils.IconButton

const val SHIP_VIEW_BOX_HEIGHT_FACTOR = 5
const val SHIP_SELECTOR_BUTTON_CORNER_RADIUS = 2
const val SHIP_SELECTOR_BUTTON_SIZE = DEFAULT_TILE_SIZE * 0.8f

// TODO: Make ship slots (containing the draggable ships on board setup) similar to this,
//  having one slot for each ship type, and only showing the empty slot when all the ships of the
//  type have been placed on the board.
/**
 * Represents a slot that presents the ship types and allows the user to select the quantity of each type in a game.
 *
 * @param shipTypes the list of ship types to be presented
 * @param onShipAdded callback that is called when the user adds a ship of the given type
 * @param onShipRemoved callback that is called when the user removes a ship of the given type
 */
@Composable
fun ShipSelector(
    shipTypes: List<ShipType>,
    onShipAdded: (ShipType) -> Unit,
    onShipRemoved: (ShipType) -> Unit
) {
    val tileSize = DEFAULT_TILE_SIZE

    Column {
        LazyRow {
            items(ShipType.values()) { shipType ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height((tileSize * SHIP_VIEW_BOX_HEIGHT_FACTOR).dp)
                            .width(tileSize.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ShipView(
                            type = shipType,
                            orientation = Orientation.VERTICAL,
                            tileSize = tileSize
                        )
                    }

                    IconButton(
                        onClick = { onShipAdded(shipType) },
                        icon = ImageVector.vectorResource(R.drawable.ic_round_add_24),
                        iconDescription = stringResource(id = R.string.increment_ship_button_icon_content_description),
                        modifier = Modifier.size(SHIP_SELECTOR_BUTTON_SIZE.dp)
                    )

                    Text(text = shipTypes.count { it == shipType }.toString())

                    IconButton(
                        onClick = { onShipRemoved(shipType) },
                        icon = ImageVector.vectorResource(R.drawable.ic_round_remove_24),
                        iconDescription = stringResource(id = R.string.decrement_ship_button_icon_content_description),
                        modifier = Modifier.size(SHIP_SELECTOR_BUTTON_SIZE.dp)
                    )
                }
            }
        }
    }
}
