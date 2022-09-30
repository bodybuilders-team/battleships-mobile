package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.configuration.BoardSetup

private enum class Page {
    MENU,
    NEW_GAME,
    SEARCH_GAME,
    SETUP_BOARD,
    GAMEPLAY
}

/**
 * The gameplay menu screen.
 *
 * @param onBackToMenuButtonPress the callback to be invoked when the back to menu button is pressed
 */
@Composable
fun GameplayMenu(onBackToMenuButtonPress: () -> Unit) {
    var currentPage by remember { mutableStateOf(Page.MENU) }

    var myBoard by remember { mutableStateOf(Board.random(16)) }
    var opponentBoard by remember { mutableStateOf(Board.random(16)) }
    var selectedCells by remember { mutableStateOf(emptyList<Coordinate>()) }

    var gameConfig by remember { mutableStateOf<GameConfig?>(null) }

    when (currentPage) {
        Page.MENU -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { currentPage = Page.NEW_GAME }) {
                Text(text = stringResource(id = R.string.gameplay_new_game_button_text))
            }

            Button(onClick = { currentPage = Page.SEARCH_GAME }) {
                Text(text = stringResource(id = R.string.gameplay_search_game_button_text))
            }

            Button(onClick = { currentPage = Page.GAMEPLAY }) {
                Text(text = "Gameplay")
            }

            Button(onClick = onBackToMenuButtonPress) {
                Text(text = stringResource(id = R.string.back_to_menu_button_text))
            }
        }

        Page.NEW_GAME -> NewGame(
            onGameConfigured = {
                gameConfig = it

                // api call to add game to lobby
                opponentBoard = Board.random(it.boardSize)

                currentPage = Page.SETUP_BOARD
            },
            onBackButtonPressed = {
                currentPage = Page.MENU
            }
        )

        Page.SEARCH_GAME -> SearchGame() /*{
            currentPage = Page.MENU
        }*/

        Page.SETUP_BOARD -> gameConfig?.let {
            BoardSetup(
                boardSize = it.boardSize,
                onBoardSetupFinished = { board ->
                    myBoard = board
                    currentPage = Page.GAMEPLAY
                },
                onBackButtonPressed = {
                    currentPage = Page.MENU
                }
            )
        }

        Page.GAMEPLAY -> Gameplay(
            myBoard = myBoard,
            opponentBoard = opponentBoard,
            selectedCells = selectedCells,
            onCellSelected = { coordinate ->
                if (!opponentBoard.getCell(coordinate).wasHit) {
                    selectedCells = if (coordinate in selectedCells) {
                        selectedCells - coordinate
                    } else {
                        selectedCells + coordinate
                    }
                }
            },
            onShootButtonPressed = {
                // api call shoot on coordinates

                // when response is received
                selectedCells = emptyList()
            },
            onResetShotsButtonPressed = {
                selectedCells = emptyList()
            },
            onBackButtonPressed = {
                currentPage = Page.MENU
            }
        )
    }
}
