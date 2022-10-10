package pt.isel.pdm.battleships.ui.screens.gameplay.newGame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
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

private const val SHIP_VIEW_BOX_HEIGHT_FACTOR = 5
private const val BUTTON_CORNER_RADIUS = 2
private const val BUTTON_PADDING = 2
private const val BUTTON_SIZE_PADDING = 2

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
            items(ShipType.values()) { ship ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.height((tileSize * SHIP_VIEW_BOX_HEIGHT_FACTOR).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ShipView(
                            type = ship,
                            orientation = Orientation.VERTICAL,
                            tileSize = tileSize
                        )
                    }

                    Button(
                        onClick = { onShipAdded(ship) },
                        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS.dp),
                        modifier = Modifier
                            .padding(bottom = BUTTON_PADDING.dp)
                            .size((DEFAULT_TILE_SIZE / BUTTON_SIZE_PADDING).dp),
                        contentPadding = PaddingValues(0.dp) // TODO: What is this?
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_add_24),
                            contentDescription =
                            stringResource(id = R.string.add_ship_button_icon_content_description)
                        )
                    }

                    Text(text = shipTypes.count { it == ship }.toString())

                    Button(
                        onClick = { onShipRemoved(ship) },
                        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS.dp),
                        modifier = Modifier.size((DEFAULT_TILE_SIZE / BUTTON_SIZE_PADDING).dp),
                        contentPadding = PaddingValues(0.dp) // TODO: What is this?
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_remove_24),
                            contentDescription =
                            stringResource(id = R.string.remove_ship_button_icon_content_description)
                        )
                    }
                }
            }
        }

        /*IconButton(
            onClick = { },
            icon = ImageVector.vectorResource(R.drawable.ic_round_directions_boat_24),
            iconDescription = "",
            text = "Select Ships"
        )*/
    }
}
