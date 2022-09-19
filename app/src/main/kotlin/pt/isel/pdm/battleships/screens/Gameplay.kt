package pt.isel.pdm.battleships.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.game.GameState
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.board.BoardView
import pt.isel.pdm.battleships.ui.board.TILE_SIZE
import pt.isel.pdm.battleships.ui.ship.UnplacedShipView

/**
 * The gameplay screen.
 *
 * @param backToMenuCallback the callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    // TODO: Gostam desta identação?
    var board by remember { mutableStateOf(Board()) }
    val shipTypes by remember { mutableStateOf(ShipType.values()) }

    var currentShipType by remember { mutableStateOf(ShipType.values().first()) }
    var selectedOrientation by remember { mutableStateOf(Orientation.VERTICAL) }
    var gameStatus by remember { mutableStateOf(GameState.PLACING_SHIPS) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.width((TILE_SIZE * Board.BOARD_SIDE_LENGTH).dp)
            ) {
                Column {
                    BoardView(board)
                    if (gameStatus == GameState.PLACING_SHIPS) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((TILE_SIZE * 5 + 10).dp), // TODO: Create Constants
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.weight(2.0f))
                            Button(
                                modifier = Modifier.weight(1.0f), // TODO: Create Constants
                                onClick = { selectedOrientation = selectedOrientation.opposite() }
                            ) {
                                Text(stringResource(R.string.gameplay_change_orientation_button_text))
                            }
                        }
                    }
                }

                val unplacedShipStartLocation = Offset(
                    x = if (selectedOrientation.isVertical()) 2 * TILE_SIZE else TILE_SIZE,
                    y = TILE_SIZE * Board.BOARD_SIDE_LENGTH +
                        (TILE_SIZE * 5 + 10) / 2 -
                        if (selectedOrientation.isVertical()) 2.5f * TILE_SIZE else TILE_SIZE
                )

                if (gameStatus == GameState.PLACING_SHIPS) {
                    UnplacedShipView(
                        initialOffset = unplacedShipStartLocation,
                        orientation = selectedOrientation,
                        size = currentShipType.size,
                        onShipPlacedCallback = { shipCoordinate ->
                            val newShip = Ship(currentShipType, shipCoordinate, selectedOrientation)
                            val canPlace = board.canPlaceShip(newShip)

                            if (canPlace) {
                                board = board.placeShip(newShip)

                                if (shipTypes.size == currentShipType.ordinal + 1) {
                                    gameStatus = GameState.RUNNING
                                } else {
                                    currentShipType = shipTypes[currentShipType.ordinal + 1]
                                }
                            }
                            canPlace
                        }
                    )
                }
            }
        }

        Button(onClick = backToMenuCallback) {
            Text(text = stringResource(id = R.string.back_to_menu_button_text))
        }
    }
}
