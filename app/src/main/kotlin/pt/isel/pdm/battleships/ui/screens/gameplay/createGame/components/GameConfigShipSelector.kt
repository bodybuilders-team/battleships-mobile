package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

/**
 * Selector that allows the user to choose the number of ships of each type.
 *
 * @param ships the current number of ships of each type
 * @param boardSize the size of the board
 * @param onShipAdded callback that is called when the user adds a ship
 * @param onShipRemoved callback that is called when the user removes a ship
 */
@Composable
fun GameConfigShipSelector(
    ships: Map<ShipType, Int>,
    boardSize: Int,
    onShipAdded: (ShipType) -> Unit,
    onShipRemoved: (ShipType) -> Unit
) {
    GameConfigSelector(
        leftSideContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.gameConfig_ships_label),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { },
                    painter = painterResource(R.drawable.ic_round_ship_24),
                    contentDescription = stringResource(R.string.gameConfig_manageShipsButton_iconDescription),
                    text = stringResource(R.string.gameConfig_manageShipsButton_text)
                )
            }
        },
        rightSideContent = {
            ShipSelector(
                shipTypes = ships,
                boardSize = boardSize,
                onShipAdded = onShipAdded,
                onShipRemoved = onShipRemoved
            )
        }
    )
}
