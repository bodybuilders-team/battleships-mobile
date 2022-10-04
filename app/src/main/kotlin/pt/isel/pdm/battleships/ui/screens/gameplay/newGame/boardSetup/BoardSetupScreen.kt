package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.board.IdentifiersWrapper
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.PLACING_MENU_PADDING
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.SHIP_SLOTS_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.ShipPlacingMenuView
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.UnplacedShipView
import pt.isel.pdm.battleships.ui.utils.GoBackButton

/**
 * Board configuration page. Allows the user to place their ships on the board how they like.
 *
 * @param navController the navigation controller
 * @param boardSize the size of the board
 * @param onBoardSetupFinished what to do when the board finished being setup
 */
@Composable
fun BoardSetupScreen(
    navController: NavController,
    boardSize: Int,
    onBoardSetupFinished: (Board) -> Unit
) {
    var board by remember { mutableStateOf(Board(boardSize)) }
    val shipTypes by remember { mutableStateOf(ShipType.values()) }

    var currentShipType by remember { mutableStateOf(ShipType.values().first()) }
    var selectedOrientation by remember { mutableStateOf(Orientation.VERTICAL) }

    val tileSize = getTileSize(boardSize)

    val initialOffset = Offset(
        x = (board.size * tileSize * SHIP_SLOTS_FACTOR) / 2 - tileSize / 2,
        y = board.size * tileSize + PLACING_MENU_PADDING +
            (board.size * tileSize * SHIP_SLOTS_FACTOR) / 2 - tileSize / 2 *
            currentShipType.size
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.width(FULL_BOARD_VIEW_BOX_SIZE.dp)) {
            IdentifiersWrapper(boardSize = board.size) {
                Column {
                    BoardView(board = board, selectedCells = emptyList(), onTileClicked = {})

                    ShipPlacingMenuView(
                        tileSize = tileSize,
                        onChangeOrientationButtonPressed = {
                            selectedOrientation = selectedOrientation.opposite()
                        },
                        onRandomBoardButtonPressed = {
                            board = Board.random(board.size)
                        },
                        onConfirmBoardButtonPressed = {
                            onBoardSetupFinished(board)
                        }
                    )
                }

                if (board.fleet.size < shipTypes.size) {
                    UnplacedShipView(
                        type = currentShipType,
                        orientation = selectedOrientation,
                        initialOffset = initialOffset,
                        boardSize = board.size,
                        onShipPlaced = { shipCoordinate ->
                            val newShip = Ship(currentShipType, shipCoordinate, selectedOrientation)
                            val canPlace = board.canPlaceShip(newShip)

                            if (canPlace) {
                                board = board.placeShip(newShip)

                                if (board.fleet.size < shipTypes.size) {
                                    currentShipType = shipTypes[currentShipType.ordinal + 1]
                                }
                            }
                        }
                    )
                }
            }
        }

        GoBackButton(navController)
    }
}
