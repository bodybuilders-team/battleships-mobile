package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.DraggableShipView
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.ShipPlacingMenuView
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing.fromPointOrNull
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.toPoint
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import kotlin.math.roundToInt

class DragState {
    var isDragging: Boolean by mutableStateOf(false)
    var initialOffset by mutableStateOf(Offset.Zero)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var shipType by mutableStateOf(ShipType.BATTLESHIP)
    var shipOrientation by mutableStateOf(Orientation.VERTICAL)
}

data class MutableShip(var ship: Ship)

/**
 * Board configuration page. Allows the user to place their ships on the board how they like.
 *
 * @param boardSize the size of the board
 * @param ships the list of ships to be placed
 * @param onBoardSetupFinished what to do when the board finished being setup
 * @param onBackButtonClicked Callback to be called when the back button is clicked
 */
@Composable
fun BoardSetupScreen(
    boardSize: Int,
    ships: List<ShipType>,
    onBoardSetupFinished: (Board) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    var board by remember { mutableStateOf(Board(boardSize)) }

    var selectedOrientation by remember { mutableStateOf(Orientation.VERTICAL) }

    val tileSize = getTileSize(boardSize)

    var unplacedShips by remember { mutableStateOf(ships) }

    val dragState by remember { mutableStateOf(DragState()) }
    var dragOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var draggableShips by remember { mutableStateOf(listOf<MutableShip>()) }
    var boardOffset: Offset by remember { mutableStateOf(Offset.Zero) }
    var currentDraggingShip by remember { mutableStateOf<MutableShip?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Column(
                modifier = Modifier.width(FULL_BOARD_VIEW_BOX_SIZE.dp)
                    .onGloballyPositioned {
                        boardOffset = it.positionInWindow()
                    }
            ) {
                BoardViewWithIdentifiers(board = board, onTileClicked = null) {
                    draggableShips.forEach { mutableShip ->
                        val (xPoint, yPoint) = mutableShip.ship.coordinate.toPoint()

                        Box(
                            modifier = Modifier
                                .offset(
                                    x = (xPoint * tileSize).dp,
                                    y = (yPoint * tileSize).dp
                                )
                                .alpha(if (currentDraggingShip == mutableShip) 0f else 1f)
                        ) {
                            DraggableShipView(
                                type = mutableShip.ship.type,
                                orientation = mutableShip.ship.orientation,
                                tileSize = tileSize,
                                dragState = dragState,
                                onDragStart = {
                                    currentDraggingShip = mutableShip
                                },
                                onDragEnd = { shipType ->
                                    val currCol =
                                        ((dragOffset.x - tileSize) / tileSize).roundToInt()
                                    val currRow =
                                        ((dragOffset.y - tileSize) / tileSize).roundToInt()

                                    Coordinate
                                        .fromPointOrNull(currCol, currRow)
                                        ?.let { coordinate ->
                                            if (
                                                Ship.isValidShipCoordinate(
                                                    coordinate,
                                                    mutableShip.ship.orientation,
                                                    shipType.size,
                                                    boardSize
                                                )
                                            ) {
                                                val newShip =
                                                    Ship(
                                                        shipType,
                                                        coordinate,
                                                        mutableShip.ship.orientation
                                                    )

                                                if (board.removeShip(mutableShip.ship)
                                                    .canPlaceShip(newShip)
                                                ) {
                                                    board = board
                                                        .removeShip(mutableShip.ship)
                                                        .placeShip(newShip)

                                                    mutableShip.ship = newShip
                                                }
                                            }
                                        }

                                    currentDraggingShip = null
                                },
                                onTap = {
                                    mutableShip.ship.coordinate
                                        .let { coordinate ->
                                            if (
                                                Ship.isValidShipCoordinate(
                                                    coordinate,
                                                    mutableShip.ship.orientation.opposite(),
                                                    mutableShip.ship.type.size,
                                                    boardSize
                                                )
                                            ) {
                                                val newShip =
                                                    Ship(
                                                        mutableShip.ship.type,
                                                        coordinate,
                                                        mutableShip.ship.orientation.opposite()
                                                    )

                                                if (board.removeShip(mutableShip.ship)
                                                    .canPlaceShip(newShip)
                                                ) {
                                                    board = board
                                                        .removeShip(mutableShip.ship)
                                                        .placeShip(newShip)

                                                    mutableShip.ship = newShip
                                                }
                                            }
                                        }
                                }
                            )
                        }
                    }
                }

                ShipPlacingMenuView(
                    shipTypes = unplacedShips,
                    tileSize = tileSize,
                    dragState = dragState,
                    onShipDragEnd = { shipType ->
                        val currCol = ((dragOffset.x - tileSize) / tileSize).roundToInt()
                        val currRow = ((dragOffset.y - tileSize) / tileSize).roundToInt()

                        Coordinate
                            .fromPointOrNull(currCol, currRow)
                            ?.let { coordinate ->
                                if (
                                    Ship.isValidShipCoordinate(
                                        coordinate,
                                        Orientation.VERTICAL,
                                        shipType.size,
                                        boardSize
                                    )
                                ) {
                                    val newShip =
                                        Ship(shipType, coordinate, selectedOrientation)
                                    val canPlace = board.canPlaceShip(newShip)

                                    if (canPlace) {
                                        board = board.placeShip(newShip)
                                        draggableShips = draggableShips + MutableShip(newShip)
                                        unplacedShips = unplacedShips - shipType
                                    }
                                }
                            }
                    },
                    onChangeOrientationButtonPressed = {
                        selectedOrientation = selectedOrientation.opposite()
                    },
                    onRandomBoardButtonPressed = {
                        draggableShips = mutableListOf()

                        board = Board.random(size = board.size, ships = ships)

                        board.fleet.forEach { ship ->
                            draggableShips = draggableShips + MutableShip(ship)
                            unplacedShips = unplacedShips - ship.type
                        }
                    },
                    onConfirmBoardButtonPressed = {
                        if (board.fleet.size == ships.size) {
                            onBoardSetupFinished(board)
                        }
                    }
                )
            }

            // Dragging composable
            if (dragState.isDragging) {
                dragOffset =
                    (dragState.initialOffset + dragState.dragOffset - boardOffset) /
                    LocalDensity.current.density

                ShipView(
                    type = dragState.shipType,
                    orientation = dragState.shipOrientation, // TODO change orientation
                    tileSize = tileSize,
                    modifier = Modifier
                        .offset(
                            x = dragOffset.x.dp,
                            y = dragOffset.y.dp
                        )
                        .border(2.dp, Color.Red)
                )
            }
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}
