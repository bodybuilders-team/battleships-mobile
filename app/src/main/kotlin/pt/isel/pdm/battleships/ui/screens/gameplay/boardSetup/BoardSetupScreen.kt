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
import java.time.Instant
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.ConfigurableBoard
import pt.isel.pdm.battleships.domain.games.game.EndGameCause
import pt.isel.pdm.battleships.domain.games.game.GamePhase
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.Player
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components.PlacedDraggableShipView
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components.ShipPlacingMenuView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.TimerView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.LeaveGameAlert
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.LeaveGameButton
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.ShipView

private const val DRAGGING_SHIP_BORDER_SIZE = 2
private const val TIME_RESYNCHRONIZATION_INTERVAL = 1000L

/**
 * Board setup screen. Allows the user to place their ships on the board how they like.
 *
 * @param boardSize the size of the board
 * @param ships the list of ships to be placed
 * @param gameState the game state
 * @param onBoardSetupFinished callback to be called when the board finished being setup
 * @param onLeaveGameButtonClicked callback to be called when the leave game button is clicked
 */
@Composable
fun BoardSetupScreen(
    boardSize: Int,
    ships: Map<ShipType, Int>,
    gameState: GameState,
    onBoardSetupFinished: (ConfigurableBoard) -> Unit,
    onLeaveGameButtonClicked: () -> Unit
) {
    var board by remember { mutableStateOf(ConfigurableBoard(boardSize)) }
    val tileSize = getTileSize(boardSize)
    val unplacedShips = remember { mutableStateMapOf<ShipType, Int>().also { it.putAll(ships) } }

    var dragState by remember { mutableStateOf(DragState()) }
    var dragOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    // Remove placed ships because we can just use the board to get them
    var placedShips by remember { mutableStateOf(listOf<Ship>()) }
    var boardOffset: Offset by remember { mutableStateOf(Offset.Zero) }

    var time by remember {
        mutableStateOf(
            Integer.max(
                (gameState.phaseEndTime - System.currentTimeMillis()).toInt(),
                0
            ) / 1000
        )
    }
    LaunchedEffect(gameState.phaseEndTime) {
        while (time > 0) {
            time =
                Integer.max((gameState.phaseEndTime - System.currentTimeMillis()).toInt(), 0) / 1000
            delay(TIME_RESYNCHRONIZATION_INTERVAL)
        }
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
                TimerView(minutes = time / 60, seconds = time % 60)

                BoardViewWithIdentifiers(board = board) {
                    placedShips.forEach { placedShip ->
                        PlacedDraggableShipView(
                            ship = placedShip,
                            tileSize = tileSize,
                            hide = dragState.ship == placedShip,
                            onDragStart = { ship, initialPosition ->
                                dragState = dragState.copy(
                                    ship = ship,
                                    isPlaced = true,
                                    dragOffset = initialPosition
                                )
                            },
                            onDragEnd = {
                                dragState = dragState.reset()

                                Coordinate
                                    .fromPointOrNull(
                                        col = ((dragOffset.x - tileSize) / tileSize)
                                            .roundToInt(),
                                        row = ((dragOffset.y - tileSize * 2) / tileSize)
                                            .roundToInt()
                                    )
                                    ?.let { coordinate ->
                                        if (
                                            Ship.isValidShipCoordinate(
                                                coordinate,
                                                placedShip.orientation,
                                                placedShip.type.size,
                                                boardSize
                                            )
                                        ) {
                                            val newShip = Ship(
                                                type = placedShip.type,
                                                coordinate = coordinate,
                                                orientation = placedShip.orientation
                                            )

                                            if (board
                                                .removeShip(placedShip)
                                                .canPlaceShip(newShip)
                                            ) {
                                                board = board
                                                    .removeShip(placedShip)
                                                    .placeShip(newShip)

                                                placedShips =
                                                    placedShips - placedShip + newShip
                                            }
                                        }
                                    }
                            },
                            onDragCancel = { dragState = dragState.reset() },
                            onDrag = { dragAmount ->
                                dragState =
                                    dragState.copy(dragOffset = dragState.dragOffset + dragAmount)
                            },
                            onTap = {
                                placedShip.coordinates.forEach { coordinate ->
                                    if (
                                        Ship.isValidShipCoordinate(
                                            coordinate,
                                            placedShip.orientation.opposite(),
                                            placedShip.type.size,
                                            boardSize
                                        )
                                    ) {
                                        val newShip = Ship(
                                            type = placedShip.type,
                                            coordinate = coordinate,
                                            orientation = placedShip.orientation.opposite()
                                        )

                                        if (board
                                            .removeShip(placedShip)
                                            .canPlaceShip(newShip)
                                        ) {
                                            board = board
                                                .removeShip(placedShip)
                                                .placeShip(newShip)

                                            placedShips =
                                                placedShips - placedShip + newShip

                                            return@PlacedDraggableShipView
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

                ShipPlacingMenuView(
                    shipTypes = unplacedShips,
                    draggingUnplaced = { shipType ->
                        dragState.ship?.type == shipType && dragState.isDragging &&
                            !dragState.isPlaced
                    },
                    onDragStart = { ship, initialPosition ->
                        dragState = dragState.copy(
                            ship = ship,
                            isPlaced = false,
                            dragOffset = initialPosition
                        )
                    },
                    onDragEnd = { ship ->
                        dragState = dragState.reset()

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
                                        placedShips = placedShips + newShip

                                        if (unplacedShips[ship.type]!! > 0) {
                                            unplacedShips[ship.type] =
                                                unplacedShips[ship.type]!! - 1
                                        }
                                    }
                                }
                            }
                    },
                    onDragCancel = { dragState = dragState.reset() },
                    onDrag = { dragAmount ->
                        dragState = dragState.copy(dragOffset = dragState.dragOffset + dragAmount)
                    },
                    onRandomBoardButtonPressed = {
                        placedShips = emptyList()

                        board = ConfigurableBoard.random(size = board.size, ships = ships)

                        placedShips = board.fleet

                        unplacedShips.keys.forEach { unplacedShips[it] = 0 }
                    },
                    onConfirmBoardButtonPressed = {
                        if (board.fleet.size == ships.values.sum())
                            onBoardSetupFinished(board)
                    }
                )

                var leavingGame by remember { mutableStateOf(false) }

                LeaveGameButton(onClick = { leavingGame = true })

                if (leavingGame)
                    LeaveGameAlert(
                        onDismissRequest = { leavingGame = false },
                        onLeaveGameButtonClicked = {
                            leavingGame = false
                            onLeaveGameButtonClicked()
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
        }
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
private data class DragState(
    val ship: Ship? = null,
    val isPlaced: Boolean = false,
    val dragOffset: Offset = Offset.Zero
) {

    val isDragging: Boolean
        get() = ship != null

    /**
     * Resets the dragging state.
     */
    fun reset() = copy(
        ship = null,
        isPlaced = false,
        dragOffset = Offset.Zero
    )
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
private fun BoardSetupScreenPreview() {
    BattleshipsScreen {
        BoardSetupScreen(
            boardSize = 10,
            ships = ShipType.defaultsMap,
            gameState = GameState(
                phase = GamePhase.DEPLOYING_FLEETS,
                phaseEndTime = Instant.now().plusMillis(45000L).toEpochMilli(),
                round = null,
                turn = null,
                winner = null,
                endCause = null
            ),
            onBoardSetupFinished = {},
            onLeaveGameButtonClicked = {}
        )
    }
}

@Preview
@Composable
private fun BoardSetupScreenEndGamePreview() {
    BattleshipsScreen {
        BoardSetupScreen(
            boardSize = 10,
            ships = ShipType.defaultsMap,
            gameState = GameState(
                phase = GamePhase.FINISHED,
                phaseEndTime = Instant.now().plusMillis(0L).toEpochMilli(),
                round = null,
                turn = null,
                winner = null,
                endCause = EndGameCause.TIMEOUT
            ),
            onBoardSetupFinished = {},
            onLeaveGameButtonClicked = {}
        )

        EndGamePopUp(
            winningPlayer = WinningPlayer.NONE,
            cause = EndGameCause.TIMEOUT,
            player = Player(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                points = 0
            ),
            opponent = Player(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                points = 0
            ),
            onPlayAgainButtonClicked = {},
            onBackToMenuButtonClicked = {}
        )
    }
}

@Preview
@Composable
private fun BoardSetupScreenMultipleQuantitiesPreview() {
    BattleshipsScreen {
        BoardSetupScreen(
            boardSize = 10,
            ships = ShipType.defaultsMap.toMutableMap().let {
                it[ShipType.BATTLESHIP] = 2
                it
            },
            gameState = GameState(
                phase = GamePhase.DEPLOYING_FLEETS,
                phaseEndTime = Instant.now().plusMillis(45000L).toEpochMilli(),
                round = null,
                turn = null,
                winner = null,
                endCause = null
            ),
            onBoardSetupFinished = {},
            onLeaveGameButtonClicked = {}
        )
    }
}
