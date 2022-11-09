package pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.ShipType

/**
 * Visual representation of a ship.
 *
 * @param type the type of the ship
 * @param orientation the orientation of the ship
 * @param tileSize the size of the tile
 * @param modifier the modifier to be applied to the ship layout
 */
@Composable
fun ShipView(
    type: ShipType,
    orientation: Orientation,
    tileSize: Float,
    modifier: Modifier = Modifier
) {
    Box(
        Modifier
            .size(
                width = (tileSize * if (orientation.isHorizontal()) type.size else 1).dp,
                height = (tileSize * if (orientation.isVertical()) type.size else 1).dp
            )
            .then(modifier)
    ) {
        ShipImage(type = type, orientation = orientation)
    }
}
