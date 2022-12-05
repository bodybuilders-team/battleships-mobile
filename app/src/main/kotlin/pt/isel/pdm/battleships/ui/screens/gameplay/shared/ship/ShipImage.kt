package pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.ShipType

private const val SHIP_CANVAS_SIZE_FACTOR = 0.8f

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
    if (type in ShipType.defaults)
        Image(
            painter = painterResource(
                if (orientation.isVertical())
                    when (type.shipName) {
                        ShipType.BATTLESHIP_NAME -> R.drawable.ship_battleship_v
                        ShipType.CARRIER_NAME -> R.drawable.ship_carrier_v
                        ShipType.CRUISER_NAME -> R.drawable.ship_cruiser_v
                        ShipType.DESTROYER_NAME -> R.drawable.ship_destroyer_v
                        ShipType.SUBMARINE_NAME -> R.drawable.ship_submarine_v
                        else -> throw IllegalArgumentException("Invalid ship type")
                    }
                else
                    when (type.shipName) {
                        ShipType.BATTLESHIP_NAME -> R.drawable.ship_battleship_h
                        ShipType.CARRIER_NAME -> R.drawable.ship_carrier_h
                        ShipType.CRUISER_NAME -> R.drawable.ship_cruiser_h
                        ShipType.DESTROYER_NAME -> R.drawable.ship_destroyer_h
                        ShipType.SUBMARINE_NAME -> R.drawable.ship_submarine_h
                        else -> throw IllegalArgumentException("Invalid ship type")
                    }
            ),
            contentDescription = stringResource(R.string.shipImage_contentDescription),
            modifier = Modifier.fillMaxSize()
        )
    else
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.DarkGray,
                size = size * SHIP_CANVAS_SIZE_FACTOR
            )
        }
}
