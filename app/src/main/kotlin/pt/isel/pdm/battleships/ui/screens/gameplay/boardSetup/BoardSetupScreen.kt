package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.ConfigurableBoard
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.PlayerInfo
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components.PlacedDraggableShipView
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components.ShipPlacingMenuView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGameCause
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.Timer
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.WinningPlayer
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.ShipView
import pt.isel.pdm.battleships.ui.screens.shared.components.GoBackButton
import kotlin.math.roundToInt

private const val DRAGGING_SHIP_BORDER_SIZE = 2

/**
 * Board setup screen. Allows the user to place their ships on the board how they like.
 *
 * @param boardSize the size of the board
 * @param ships the list of ships to be placed
 * @param time the time the player has to place the ships
 * @param onTimeChanged callback to be called when the time changes
 * @param playerInfo the player's info
 * @param opponentInfo the opponent's info
 * @param onBoardSetupFinished callback to be called when the board finished being setup
 * @param onBackButtonClicked callback to be called when the back button is clicked
 * @param onPlayAgainButtonClicked callback to be called when the play again button is clicked
 * @param onBackToMenuButtonClicked callback to be called when the back to menu button is clicked
 */
@Composable
fun BoardSetupScreen(
    boardSize: Int,
    ships: Map<ShipType, Int>,
    time: Int,
    onTimeChanged: (Int) -> Unit,
    playerInfo: PlayerInfo,
    opponentInfo: PlayerInfo,
    onBoardSetupFinished: (ConfigurableBoard) -> Unit,
    onBackButtonClicked: () -> Unit,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    var board by remember { mutableStateOf(ConfigurableBoard(boardSize)) }
    val tileSize = getTileSize(boardSize)
    val unplacedShips = remember { mutableStateMapOf<ShipType, Int>().also { it.putAll(ships) } }

    val dragState by remember { mutableStateOf(DragState()) }
    var dragOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var draggableShips by remember { mutableStateOf(listOf<Ship>()) }
    var boardOffset: Offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(time) {
        delay(1000)
        if (time > 0)
            onTimeChanged(time - 1)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Column(
                modifier = Modifier
                    .width(FULL_BOARD_VIEW_BOX_SIZE.dp)
                    .onGloballyPositioned { boardOffset = it.positionInWindow() }
            ) {
                Timer(minutes = time / 60, seconds = time % 60)

                BoardViewWithIdentifiers(board = board) {
                    draggableShips.forEach { draggableShip ->
                        PlacedDraggableShipView(
                            ship = draggableShip,
                            tileSize = tileSize,
                            hide = dragState.ship == draggableShip,
                            onDragStart = { _, initialPosition ->
                                dragState.ship = draggableShip
                                dragState.dragOffset = initialPosition
                            },
                            onDragEnd = {
                                dragState.reset()

                                Coordinate
                                    .fromPointOrNull(
                                        col = ((dragOffset.x - tileSize) / tileSize).roundToInt(),
                                        row = ((dragOffset.y - tileSize * 2) / tileSize).roundToInt()
                                    )
                                    ?.let { coordinate ->
                                        if (
                                            Ship.isValidShipCoordinate(
                                                coordinate,
                                                draggableShip.orientation,
                                                draggableShip.type.size,
                                                boardSize
                                            )
                                        ) {
                                            val newShip = Ship(
                                                type = draggableShip.type,
                                                coordinate = coordinate,
                                                orientation = draggableShip.orientation
                                            )

                                            if (board
                                                .removeShip(draggableShip)
                                                .canPlaceShip(newShip)
                                            ) {
                                                board = board
                                                    .removeShip(draggableShip)
                                                    .placeShip(newShip)

                                                draggableShips =
                                                    draggableShips - draggableShip + newShip
                                            }
                                        }
                                    }
                            },
                            onDragCancel = { dragState.reset() },
                            onDrag = { dragAmount -> dragState.dragOffset += dragAmount },
                            onTap = {
                                draggableShip.coordinate
                                    .let { coordinate ->
                                        if (
                                            Ship.isValidShipCoordinate(
                                                coordinate,
                                                draggableShip.orientation.opposite(),
                                                draggableShip.type.size,
                                                boardSize
                                            )
                                        ) {
                                            val newShip = Ship(
                                                type = draggableShip.type,
                                                coordinate = coordinate,
                                                orientation = draggableShip.orientation.opposite()
                                            )

                                            if (board
                                                .removeShip(draggableShip)
                                                .canPlaceShip(newShip)
                                            ) {
                                                board = board
                                                    .removeShip(draggableShip)
                                                    .placeShip(newShip)

                                                draggableShips =
                                                    draggableShips - draggableShip + newShip
                                            }
                                        }
                                    }
                            }
                        )
                    }
                }

                ShipPlacingMenuView(
                    shipTypes = unplacedShips,
                    dragging = { shipType ->
                        dragState.ship?.type == shipType && dragState.isDragging
                    },
                    onDragStart = { ship, initialPosition ->
                        dragState.ship = ship
                        dragState.dragOffset = initialPosition
                    },
                    onDragEnd = { ship ->
                        dragState.reset()

                        Coordinate
                            .fromPointOrNull(
                                col = ((dragOffset.x - tileSize) / tileSize).roundToInt(),
                                row = ((dragOffset.y - tileSize * 2) / tileSize).roundToInt()
                            )
                            ?.let { coordinate ->
                                if (
                                    Ship.isValidShipCoordinate(
                                        coordinate = coordinate,
                                        orientation = Orientation.VERTICAL,
                                        size = ship.type.size,
                                        boardSize = boardSize
                                    )
                                ) {
                                    val newShip = Ship(ship.type, coordinate, Orientation.VERTICAL)

                                    if (board.canPlaceShip(newShip)) {
                                        board = board.placeShip(newShip)
                                        draggableShips = draggableShips + newShip

                                        if (unplacedShips[ship.type]!! > 0) {
                                            unplacedShips[ship.type] =
                                                unplacedShips[ship.type]!! - 1
                                        }
                                    }
                                }
                            }
                    },
                    onDragCancel = { dragState.reset() },
                    onDrag = { dragAmount -> dragState.dragOffset += dragAmount },
                    onRandomBoardButtonPressed = {
                        draggableShips = emptyList()

                        board = ConfigurableBoard.random(size = board.size, ships = ships)

                        draggableShips = board.fleet

                        unplacedShips.keys.forEach { unplacedShips[it] = 0 }
                    },
                    onConfirmBoardButtonPressed = {
                        if (board.fleet.size == ships.values.sum())
                            onBoardSetupFinished(board)
                    }
                )
            }

            // Dragging composable
            if (dragState.isDragging) {
                dragOffset = (dragState.dragOffset - boardOffset) / LocalDensity.current.density

                dragState.ship?.let { ship ->
                    ShipView(
                        type = ship.type,
                        orientation = ship.orientation,
                        tileSize = tileSize,
                        modifier = Modifier
                            .offset(dragOffset.x.dp, dragOffset.y.dp)
                            .border(DRAGGING_SHIP_BORDER_SIZE.dp, Color.Red)
                    )
                }
            }

            if (time == 0)
                EndGamePopUp(
                    winningPlayer = WinningPlayer.NONE,
                    cause = EndGameCause.TIMEOUT,
                    pointsWon = 0,
                    playerInfo = playerInfo,
                    opponentInfo = opponentInfo,
                    onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                    onBackToMenuButtonClicked = onBackToMenuButtonClicked
                )
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}

/**
 * Stores the state of the ship dragging in the board setup screen.
 * Whenever a ship is dragged, the instance of this class is updated with the ship information,
 * therefore each ship does not hold a dragging state of its own.
 *
 * @property ship the ship being dragged
 * @property dragOffset the drag offset
 * @property isDragging whether the ship is being dragged
 */
private class DragState {
    var ship by mutableStateOf<Ship?>(null)
    var dragOffset by mutableStateOf(Offset.Zero)

    val isDragging: Boolean
        get() = ship != null

    /**
     * Resets the dragging state.
     */
    fun reset() {
        ship = null
        dragOffset = Offset.Zero
    }
}

/**
 * Gets a coordinate from a point or null if it isn't valid.
 *
 * @param col the column of the point
 * @param row the row of the point
 *
 * @return the coordinate or null if it isn't valid
 */
private fun Coordinate.Companion.fromPointOrNull(col: Int, row: Int): Coordinate? = when {
    isValid(Board.FIRST_COL + col, row + 1) -> Coordinate(Board.FIRST_COL + col, row + 1)
    else -> null
}

@Preview
@Composable
fun BoardSetupScreenPreview() {
    var timer by remember { mutableStateOf(30) }

    BattleshipsScreen {
        BoardSetupScreen(
            boardSize = 10,
            ships = ShipType.defaultsMap,
            time = timer,
            onTimeChanged = { timer -= 1 },
            playerInfo = PlayerInfo(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 0
            ),
            opponentInfo = PlayerInfo(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 0
            ),
            onBoardSetupFinished = {},
            onBackButtonClicked = {},
            onPlayAgainButtonClicked = {},
            onBackToMenuButtonClicked = {}
        )
    }
}

@Preview
@Composable
fun BoardSetupScreenEndGamePreview() {
    BattleshipsScreen {
        BoardSetupScreen(
            boardSize = 10,
            ships = ShipType.defaultsMap,
            time = 0,
            onTimeChanged = {},
            onBoardSetupFinished = {},
            onBackButtonClicked = {},
            playerInfo = PlayerInfo(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 0
            ),
            opponentInfo = PlayerInfo(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 0
            ),
            onPlayAgainButtonClicked = {},
            onBackToMenuButtonClicked = {}
        )
    }
}
