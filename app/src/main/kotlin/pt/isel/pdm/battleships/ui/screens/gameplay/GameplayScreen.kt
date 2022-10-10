package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TileSelectionView
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.PlacedShipView
import pt.isel.pdm.battleships.ui.utils.GoBackButton

/**
 * The gameplay screen.
 *
 * @param navController the navigation controller
 * @param myBoard the player's board
 * @param opponentBoard the opponent's board
 * @param selectedCells the cells that are currently selected
 * @param onCellSelected the callback to be invoked when the player selects a cell
 * @param onShootButtonPressed the callback to be invoked when the player presses the shoot button
 * @param onResetShotsButtonPressed the callback to be invoked when the player presses the
 * reset shots button
 */
@Composable
fun GameplayScreen(
    navController: NavController,
    myBoard: Board,
    opponentBoard: Board,
    selectedCells: List<Coordinate>,
    onCellSelected: (Coordinate) -> Unit,
    onShootButtonPressed: () -> Unit,
    onResetShotsButtonPressed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.opponent_board_description))

        val opponentBoardTileSize = getTileSize(opponentBoard.size)

        BoardViewWithIdentifiers(
            board = opponentBoard,
            onTileClicked = onCellSelected
        ) {
            opponentBoard.fleet.forEach { ship ->
                PlacedShipView(ship, opponentBoardTileSize)
            }
            selectedCells.forEach {
                TileSelectionView(it, opponentBoardTileSize)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.my_board_description))

                val myBoardTileSize = getTileSize(myBoard.size) * SMALLER_BOARD_TILE_SIZE_FACTOR

                BoardViewWithIdentifiers(
                    board = myBoard,
                    onTileClicked = null,
                    tileSizeFactor = SMALLER_BOARD_TILE_SIZE_FACTOR
                ) {
                    myBoard.fleet.forEach { ship ->
                        PlacedShipView(ship, myBoardTileSize)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onShootButtonPressed) {
                    Text(stringResource(id = R.string.gameplay_shoot_button_text))
                }
                Button(onClick = onResetShotsButtonPressed) {
                    Text(stringResource(id = R.string.gameplay_reset_shots_button_text))
                }
            }
        }

        GoBackButton(navController)
    }
}

private const val SMALLER_BOARD_TILE_SIZE_FACTOR = 0.5f
