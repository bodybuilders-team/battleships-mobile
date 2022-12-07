package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.SMALLER_BOARD_TILE_SIZE_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.TileHitView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.PlacedShipView

/**
 * View that shows the player's board.
 *
 * @param myBoard the player's board
 * @param myTurn whether it's the player's turn
 */
@Composable
fun MyBoardView(myBoard: MyBoard, myTurn: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.gameplay_myBoard_description))

        BoardViewWithIdentifiers(
            board = myBoard,
            tileSizeFactor = if (!myTurn) 1.0f else SMALLER_BOARD_TILE_SIZE_FACTOR
        ) {
            val tileSize = getTileSize(myBoard.size) *
                if (!myTurn) 1.0f
                else SMALLER_BOARD_TILE_SIZE_FACTOR

            myBoard.fleet.forEach { ship -> PlacedShipView(ship = ship, tileSize = tileSize) }

            Row {
                repeat(myBoard.size) { colIdx ->
                    Column {
                        repeat(myBoard.size) { rowIdx ->
                            val coordinate = Coordinate(
                                col = Board.FIRST_COL + colIdx,
                                row = Board.FIRST_ROW + rowIdx
                            )

                            Box(modifier = Modifier.size(tileSize.dp)) {
                                val cell = myBoard.getCell(coordinate)
                                if (cell.wasHit) {
                                    TileHitView(
                                        tileSize = tileSize,
                                        hitShip = cell is ShipCell
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
