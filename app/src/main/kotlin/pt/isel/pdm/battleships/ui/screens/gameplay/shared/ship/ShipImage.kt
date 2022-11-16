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
                id = when (type.shipName) {
                    ShipType.BATTLESHIP -> if (orientation.isVertical()) R.drawable.ship_battleship_v
                    else R.drawable.ship_battleship_h
                    ShipType.CARRIER -> if (orientation.isVertical()) R.drawable.ship_carrier_v
                    else R.drawable.ship_carrier_h
                    ShipType.CRUISER -> if (orientation.isVertical()) R.drawable.ship_cruiser_v
                    else R.drawable.ship_cruiser_h
                    ShipType.DESTROYER -> if (orientation.isVertical()) R.drawable.ship_destroyer_v
                    else R.drawable.ship_destroyer_h
                    ShipType.SUBMARINE -> if (orientation.isVertical()) R.drawable.ship_submarine_v
                    else R.drawable.ship_submarine_h
                    else -> throw IllegalArgumentException("Invalid ship type")
                }
            ),
            contentDescription = stringResource(id = R.string.ship_image_content_description),
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
