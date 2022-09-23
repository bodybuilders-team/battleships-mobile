package pt.isel.pdm.battleships.ui.screens.gameplay.ship

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.ShipType

/**
 * Draws the ship image.
 *
 * @param type the type of the ship
 * @param orientation the orientation of the ship
 */
@Composable
fun ShipImage(
    type: ShipType,
    orientation: Orientation
) {
    Image(
        painter = painterResource(
            id = when (type) {
                ShipType.BATTLESHIP -> R.drawable.ship_battleship
                ShipType.CARRIER -> R.drawable.ship_carrier
                ShipType.CRUISER -> R.drawable.ship_cruiser
                ShipType.DESTROYER -> R.drawable.ship_destroyer
                ShipType.SUBMARINE -> R.drawable.ship_submarine
            }
        ),
        contentDescription = "Ship",
        modifier = Modifier
            .rotate(degrees = if (orientation == Orientation.VERTICAL) 0f else 90f)
            .fillMaxSize() // FIXME: ships are still small in horizontal orientation
    )
}
