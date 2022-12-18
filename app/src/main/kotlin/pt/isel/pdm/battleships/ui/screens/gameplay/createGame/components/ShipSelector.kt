package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.DEFAULT_TILE_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.ShipView
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

const val SHIP_VIEW_BOX_HEIGHT_FACTOR = 5
private const val SHIP_SELECTOR_BUTTON_SIZE = DEFAULT_TILE_SIZE * 0.8f
private const val MAX_BOARD_OCCUPANCY_PERCENTAGE = 0.5f
const val MIN_SHIP_QUANTITY = 0
const val MAX_SHIP_QUANTITY = 5

/**
 * A slot that presents the ship types and allows the user to select the quantity of each type in a game.
 *
 * @param shipTypes the list of ship types to be presented
 * @param boardSize the size of the board
 * @param onShipAdded callback that is called when the user adds a ship of the given type
 * @param onShipRemoved callback that is called when the user removes a ship of the given type
 */
@Composable
fun ShipSelector(
    shipTypes: Map<ShipType, Int>,
    boardSize: Int,
    onShipAdded: (ShipType) -> Unit,
    onShipRemoved: (ShipType) -> Unit
) {
    val tileSize = DEFAULT_TILE_SIZE

    val totalTilesOccupied = shipTypes.entries.fold(0) { acc, (ship, count) ->
        acc + ship.size * count
    }

    Column {
        LazyRow {
            items(shipTypes.entries.toList()) { (shipType, quantity) ->
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
                        painter = painterResource(R.drawable.ic_round_add_24),
                        contentDescription = stringResource(R.string.gameConfig_incrementShipButtonIcon_contentDescription),
                        modifier = Modifier.size(SHIP_SELECTOR_BUTTON_SIZE.dp),
                        enabled = quantity < MAX_SHIP_QUANTITY || totalTilesOccupied + shipType.size
                            < boardSize * boardSize * MAX_BOARD_OCCUPANCY_PERCENTAGE
                    )

                    Text(text = quantity.toString())

                    IconButton(
                        onClick = { onShipRemoved(shipType) },
                        painter = painterResource(R.drawable.ic_round_remove_24),
                        contentDescription = stringResource(R.string.gameConfig_decrementShipButtonIcon_contentDescription),
                        modifier = Modifier.size(SHIP_SELECTOR_BUTTON_SIZE.dp),
                        enabled = quantity > MIN_SHIP_QUANTITY
                    )
                }
            }
        }
    }
}
