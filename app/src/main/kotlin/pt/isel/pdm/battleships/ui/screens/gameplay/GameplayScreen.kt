package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TileSelectionView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.PlacedShipView
import pt.isel.pdm.battleships.ui.utils.GoBackButton

/**
 * The gameplay screen.
 *
 * @param board the board to be displayed.
 * @param gameConfig the game configuration.
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun GameplayScreen(
    board: Board,
    gameConfig: GameConfig,
    onBackButtonClicked: () -> Unit
) {
    val myBoard by remember { mutableStateOf(board) }
    val opponentBoard by remember { mutableStateOf(Board.random()) }
    var selectedCells by remember { mutableStateOf(listOf<Coordinate>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.opponent_board_description))

        BoardViewWithIdentifiers(board = opponentBoard) {
            val opponentBoardTileSize = getTileSize(opponentBoard.size)

            opponentBoard.fleet.forEach { ship ->
                PlacedShipView(ship, opponentBoardTileSize)
            }

            Row {
                repeat(board.size) { rowIdx ->
                    Column {
                        repeat(board.size) { colIdx ->
                            val coordinate = Coordinate(FIRST_COL + colIdx, FIRST_ROW + rowIdx)
                            TileSelectionView(
                                tileSize = opponentBoardTileSize,
                                selected = coordinate in selectedCells,
                                onTileClicked = {
                                    if (!opponentBoard.getCell(coordinate).wasHit) {
                                        if (coordinate in selectedCells) {
                                            selectedCells = selectedCells - coordinate
                                        } else if (selectedCells.size < gameConfig.shotsPerTurn) {
                                            selectedCells = selectedCells + coordinate
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.my_board_description))

                BoardViewWithIdentifiers(
                    board = myBoard,
                    tileSizeFactor = SMALLER_BOARD_TILE_SIZE_FACTOR
                ) {
                    myBoard.fleet.forEach { ship ->
                        PlacedShipView(
                            ship = ship,
                            tileSize = getTileSize(myBoard.size) * SMALLER_BOARD_TILE_SIZE_FACTOR
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { selectedCells = emptyList() }) {
                    Text(stringResource(id = R.string.gameplay_shoot_button_text))
                }
                Button(onClick = { selectedCells = emptyList() }) {
                    Text(stringResource(id = R.string.gameplay_reset_shots_button_text))
                }
            }
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}

private const val SMALLER_BOARD_TILE_SIZE_FACTOR = 0.5f
