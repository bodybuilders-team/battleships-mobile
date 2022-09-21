package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TILE_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.UnplacedShipView

/**
 * The gameplay screen.
 *
 * @param backToMenuCallback the callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    var board by remember { mutableStateOf(Board()) }
    val shipTypes by remember { mutableStateOf(ShipType.values()) }

    var currentShipType by remember { mutableStateOf(ShipType.values().first()) }
    var selectedOrientation by remember { mutableStateOf(Orientation.VERTICAL) }
    var gameStatus by remember { mutableStateOf(GameState.PLACING_SHIPS) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Box to allow dragging the ships
        Box(modifier = Modifier.width((TILE_SIZE * (board.size + 1)).dp)) {
            Column {
                BoardView(board)

                // Placing Menu
                if (gameStatus == GameState.PLACING_SHIPS) {
                    ShipPlacingMenu(
                        onSelectedOrientation = {
                            selectedOrientation = selectedOrientation.opposite()
                        },
                        onRandomBoardButtonClick = {
                            board = Board(board.size)
                            gameStatus = GameState.IN_PROGRESS
                        }
                    )
                }
            }

            // UnplacedShipView
            if (gameStatus == GameState.PLACING_SHIPS) {
                UnplacedShipView(
                    initialOffset = Offset(
                        x = ((board.size + 1) * TILE_SIZE * SHIP_SLOTS_FACTOR) / 2 - TILE_SIZE / 2 *
                            if (selectedOrientation.isVertical()) 1 else currentShipType.size,
                        y = (board.size + 1) * TILE_SIZE + PLACING_MENU_PADDING +
                            ((board.size + 1) * TILE_SIZE * SHIP_SLOTS_FACTOR) / 2 - TILE_SIZE / 2 *
                            if (selectedOrientation.isHorizontal()) 1 else currentShipType.size
                    ),
                    boardOffset = Offset(x = TILE_SIZE, y = TILE_SIZE),
                    orientation = selectedOrientation,
                    size = currentShipType.size,
                    boardSize = board.size,
                    onShipPlacedCallback = { shipCoordinate ->
                        val newShip = Ship(currentShipType, shipCoordinate, selectedOrientation)
                        val canPlace = board.canPlaceShip(newShip)

                        if (canPlace) {
                            board = board.placeShip(newShip)

                            if (shipTypes.size == currentShipType.ordinal + 1) {
                                gameStatus = GameState.IN_PROGRESS
                            } else {
                                currentShipType = shipTypes[currentShipType.ordinal + 1]
                            }
                        }
                        canPlace
                    }
                )
            }
        }

        Button(onClick = backToMenuCallback) {
            Text(text = stringResource(id = R.string.back_to_menu_button_text))
        }
    }
}
