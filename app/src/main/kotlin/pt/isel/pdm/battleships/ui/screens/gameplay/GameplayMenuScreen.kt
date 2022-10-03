package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.configuration.BoardSetup
import pt.isel.pdm.battleships.ui.utils.BackButton

/**
 * The gameplay menu screen.
 *
 * @param navController the navigation controller
 */
@Composable
fun GameplayMenuScreen(navController: NavController) {
    var myBoard by remember { mutableStateOf(Board.random(16)) }
    var opponentBoard by remember { mutableStateOf(Board.random(16)) }
    var selectedCells by remember { mutableStateOf(emptyList<Coordinate>()) }
    var gameConfig by remember { mutableStateOf<GameConfig?>(null) }

    val innerNavController = rememberNavController()
    NavHost(
        navController = innerNavController,
        startDestination = "gameplay/menu"
    ) {
        composable("gameplay/menu") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(onClick = { innerNavController.navigate("gameplay/new-game") }) {
                    Text(text = stringResource(id = R.string.gameplay_new_game_button_text))
                }

                Button(onClick = { innerNavController.navigate("gameplay/search-game") }) {
                    Text(text = stringResource(id = R.string.gameplay_search_game_button_text))
                }

                Button(onClick = { innerNavController.navigate("gameplay/gameplay") }) {
                    Text(text = "Gameplay")
                }

                BackButton(navController)
            }
        }
        composable("gameplay/new-game") {
            NewGame(
                innerNavController,
                onGameConfigured = {
                    gameConfig = it

                    // api call to add game to lobby
                    opponentBoard = Board.random(it.boardSize)

                    innerNavController.navigate("gameplay/setup-board")
                }
            )
        }
        composable("gameplay/search-game") {
            SearchGame(innerNavController)
        }
        composable("gameplay/setup-board") {
            gameConfig?.let {
                BoardSetup(
                    innerNavController,
                    boardSize = it.boardSize,
                    onBoardSetupFinished = { board ->
                        myBoard = board
                        innerNavController.navigate("gameplay/gameplay")
                    }
                )
            }
        }
        composable("gameplay/gameplay") {
            Gameplay(
                innerNavController,
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
                }
            )
        }
    }
}
