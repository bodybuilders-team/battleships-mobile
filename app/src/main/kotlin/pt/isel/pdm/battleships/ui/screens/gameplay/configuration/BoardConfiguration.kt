package pt.isel.pdm.battleships.ui.screens.gameplay.configuration

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
import pt.isel.pdm.battleships.ui.screens.gameplay.PLACING_MENU_PADDING
import pt.isel.pdm.battleships.ui.screens.gameplay.SHIP_SLOTS_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.ShipPlacingMenu
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.UnplacedShipView

/**
 * Board configuration page. Allows the user to place their ships on the board how they like.
 *
 * @param boardSize the size of the board
 * @param onBackButtonPressed what to do when the back button is pressed
 */
@Composable
fun BoardConfiguration(boardSize: Int, onBackButtonPressed: () -> Unit) {
    var board by remember { mutableStateOf(Board(boardSize)) }
    val shipTypes by remember { mutableStateOf(ShipType.values()) }

    var currentShipType by remember { mutableStateOf(ShipType.values().first()) }
    var selectedOrientation by remember { mutableStateOf(Orientation.VERTICAL) }
    var gameStatus by remember { mutableStateOf(GameState.PLACING_SHIPS) }

    val tileSize = getTileSize(boardSize)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Box to allow dragging the ships
        Box(modifier = Modifier.width(FULL_BOARD_VIEW_BOX_SIZE.dp)) {
            Column {
                BoardView(board)

                // Placing Menu
                if (gameStatus == GameState.PLACING_SHIPS) {
                    ShipPlacingMenu(
                        onSelectedOrientation = {
                            selectedOrientation = selectedOrientation.opposite()
                        },
                        onRandomBoardButtonClick = {
                            board = Board.random(board.size)
                            gameStatus = GameState.IN_PROGRESS
                        }
                    )
                }
            }

            // UnplacedShipView
            if (gameStatus == GameState.PLACING_SHIPS) {
                UnplacedShipView(
                    initialOffset = Offset(
                        x = ((board.size + 1) * tileSize * SHIP_SLOTS_FACTOR) / 2 - tileSize / 2 *
                            if (selectedOrientation.isVertical()) 1 else currentShipType.size,
                        y = (board.size + 1) * tileSize + PLACING_MENU_PADDING +
                            ((board.size + 1) * tileSize * SHIP_SLOTS_FACTOR) / 2 - tileSize / 2 *
                            if (selectedOrientation.isHorizontal()) 1 else currentShipType.size
                    ),
                    boardOffset = Offset(x = tileSize, y = tileSize),
                    orientation = selectedOrientation,
                    size = currentShipType.size,
                    boardSize = board.size,
                    onShipPlaced = { shipCoordinate ->
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

        Button(onClick = onBackButtonPressed) {
            Text(text = stringResource(id = R.string.back_button_text))
        }
    }
}
