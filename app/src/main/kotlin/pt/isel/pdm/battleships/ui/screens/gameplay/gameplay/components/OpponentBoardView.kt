package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.SMALLER_BOARD_TILE_SIZE_FACTOR
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.TileHitView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.TileSelectionView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.PlacedShipView

/**
 * View that shows the opponent's board.
 *
 * @param opponentBoard the opponent's board
 * @param selectedCells the selected cells
 * @param onTileClicked the callback to be invoked when a tile is clicked
 */
@Composable
fun OpponentBoardView(
    opponentBoard: OpponentBoard,
    myTurn: Boolean,
    selectedCells: List<Coordinate>,
    onTileClicked: (Coordinate) -> Unit
) {
    BoardViewWithIdentifiers(
        board = opponentBoard,
        tileSizeFactor = if (myTurn) 1.0f else SMALLER_BOARD_TILE_SIZE_FACTOR
    ) {
        val tileSize = getTileSize(opponentBoard.size) *
            if (myTurn) 1.0f
            else SMALLER_BOARD_TILE_SIZE_FACTOR

        opponentBoard.fleet.forEach { ship ->
            PlacedShipView(ship, tileSize)
        }

        Row {
            repeat(opponentBoard.size) { colIdx ->
                Column {
                    repeat(opponentBoard.size) { rowIdx ->
                        val coordinate = Coordinate(
                            col = Board.FIRST_COL + colIdx,
                            row = Board.FIRST_ROW + rowIdx
                        )

                        Box {
                            TileSelectionView(
                                tileSize = tileSize,
                                selectionEnabled = myTurn,
                                selected = coordinate in selectedCells,
                                onTileClicked = { onTileClicked(coordinate) }
                            )

                            TileHitView(
                                tileSize = tileSize,
                                hit = opponentBoard.getCell(coordinate).wasHit
                            )
                        }
                    }
                }
            }
        }
    }
}
