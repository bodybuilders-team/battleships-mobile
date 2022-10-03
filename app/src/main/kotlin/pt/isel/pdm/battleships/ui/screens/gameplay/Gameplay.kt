package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.screens.gameplay.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.board.FULL_BOARD_VIEW_BOX_SIZE
import pt.isel.pdm.battleships.ui.utils.BackButton

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
fun Gameplay(
    navController: NavController,
    myBoard: Board,
    opponentBoard: Board,
    selectedCells: List<Coordinate>,
    onCellSelected: (Coordinate) -> Unit,
    onShootButtonPressed: () -> Unit,
    onResetShotsButtonPressed: () -> Unit
) {
    Column(
        modifier = Modifier.width(FULL_BOARD_VIEW_BOX_SIZE.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Opponent Board")
        BoardViewWithIdentifiers(
            board = opponentBoard,
            selectedCells = selectedCells,
            onTileClicked = onCellSelected
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("My Board")
                BoardViewWithIdentifiers(
                    board = myBoard,
                    selectedCells = emptyList(),
                    onTileClicked = null,
                    tileSizeFactor = 0.5f
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onShootButtonPressed) {
                    Text("Shoot!")
                }
                Button(onClick = onResetShotsButtonPressed) {
                    Text("Reset shots")
                }
            }
        }

        BackButton(navController)
    }
}
