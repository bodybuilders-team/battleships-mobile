package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.game.GameState
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.UnplacedShipView
import pt.isel.pdm.battleships.ui.board.BoardView
import pt.isel.pdm.battleships.ui.board.TILE_SIZE

/**
 * The gameplay screen.
 *
 * @param backToMenuCallback the callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    // TODO: Implement the gameplay screen

    var board by remember {
        mutableStateOf(Board())
    }

    val shipTypes by remember {
        mutableStateOf(ShipType.values())
    }

    var currentShipType by remember {
        mutableStateOf(ShipType.values()[0])
    }

    var selectedOrientation by remember {
        mutableStateOf(Orientation.VERTICAL)
    }

    var gameStatus by remember {
        mutableStateOf(GameState.PLACING_SHIPS)
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(Modifier.width((TILE_SIZE * Board.BOARD_SIDE_LENGTH).dp)) {
                Column {
                    BoardView(board)
                    if (gameStatus == GameState.PLACING_SHIPS) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height((TILE_SIZE * 5 + 10).dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.weight(2.0f)) {
                            }
                            Button(
                                modifier = Modifier.weight(1.0f),
                                onClick = {
                                    selectedOrientation =
                                        if (selectedOrientation == Orientation.HORIZONTAL) {
                                            Orientation.VERTICAL
                                        } else {
                                            Orientation.HORIZONTAL
                                        }
                                }
                            ) {
                                Text("Change Orientation")
                            }
                        }
                    }
                }

                val unplacedShipStartLocation = Offset(
                    if (selectedOrientation == Orientation.VERTICAL) {
                        2 * TILE_SIZE
                    } else TILE_SIZE,
                    TILE_SIZE * Board.BOARD_SIDE_LENGTH + (TILE_SIZE * 5 + 10) / 2 -
                            if (selectedOrientation == Orientation.VERTICAL) {
                                2.5f * TILE_SIZE
                            } else TILE_SIZE
                )

                if (gameStatus == GameState.PLACING_SHIPS) {
                    UnplacedShipView(
                        initialOffset = unplacedShipStartLocation,
                        orientation = selectedOrientation,
                        size = currentShipType.size,
                        onShipPlacedCallback = { shipCoord ->
                            val coordinateList = mutableListOf<Coordinate>()

                            repeat(currentShipType.size) { i ->
                                coordinateList.add(
                                    Coordinate(
                                        shipCoord.col +
                                                if (selectedOrientation == Orientation.HORIZONTAL) {
                                                    i
                                                } else 0,
                                        shipCoord.row +
                                                if (selectedOrientation == Orientation.VERTICAL) {
                                                    i
                                                } else 0
                                    )
                                )
                            }

                            val newShip = Ship(currentShipType, coordinateList)

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