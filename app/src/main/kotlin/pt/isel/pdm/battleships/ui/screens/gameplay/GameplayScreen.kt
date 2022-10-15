package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.board.MyBoard
import pt.isel.pdm.battleships.domain.board.OpponentBoard
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TileSelectionView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.PlacedShipView
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.IconButton

/**
 * The gameplay screen.
 *
 * @param myBoard the player's board
 * @param gameConfig the game configuration
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked
 */
@Composable
fun GameplayScreen(
    myBoard: MyBoard,
    gameConfig: GameConfig,
    onShootClicked: (List<Coordinate>) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val opponentBoard by remember { mutableStateOf(OpponentBoard(myBoard.size)) }
    var selectedCells by remember { mutableStateOf(listOf<Coordinate>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.opponent_board_description))

        OpponentBoardView(
            opponentBoard = opponentBoard,
            selectedCells = selectedCells,
            onTileClicked = { coordinate ->
                if (!opponentBoard.getCell(coordinate).wasHit) {
                    if (coordinate in selectedCells) {
                        selectedCells = selectedCells - coordinate
                    } else if (selectedCells.size < gameConfig.shotsPerTurn) {
                        selectedCells = selectedCells + coordinate
                    }
                }
            }
        )

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
                IconButton(
                    onClick = {
                        onShootClicked(selectedCells)
                        selectedCells = emptyList()
                    },
                    icon = ImageVector.vectorResource(R.drawable.ic_round_send_diagonal_24),
                    iconDescription = stringResource(id = R.string.gameplay_shoot_button_description),
                    text = stringResource(id = R.string.gameplay_shoot_button_text)
                )
                IconButton(
                    onClick = { selectedCells = emptyList() },
                    icon = ImageVector.vectorResource(R.drawable.ic_round_reset_24),
                    iconDescription = stringResource(id = R.string.gameplay_reset_shots_button_description),
                    text = stringResource(id = R.string.gameplay_reset_shots_button_text)
                )
            }
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}

private const val SMALLER_BOARD_TILE_SIZE_FACTOR = 0.5f

/**
 * View that shows the opponent's board.
 *
 * @param opponentBoard the opponent's board
 * @param selectedCells the selected cells
 * @param onTileClicked the callback to be invoked when a tile is clicked
 */
@Composable
private fun OpponentBoardView(
    opponentBoard: OpponentBoard,
    selectedCells: List<Coordinate>,
    onTileClicked: (Coordinate) -> Unit
) {
    BoardViewWithIdentifiers(board = opponentBoard) {
        val opponentBoardTileSize = getTileSize(opponentBoard.size)

        opponentBoard.fleet.forEach { ship ->
            PlacedShipView(ship, opponentBoardTileSize)
        }

        Row {
            repeat(opponentBoard.size) { rowIdx ->
                Column {
                    repeat(opponentBoard.size) { colIdx ->
                        val coordinate = Coordinate(FIRST_COL + colIdx, FIRST_ROW + rowIdx)
                        TileSelectionView(
                            tileSize = opponentBoardTileSize,
                            selected = coordinate in selectedCells,
                            onTileClicked = { onTileClicked(coordinate) }
                        )
                    }
                }
            }
        }
    }
}
