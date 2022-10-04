package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.NewGameScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.BoardSetupScreen
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.MenuButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

/**
 * The gameplay menu screen.
 *
 * @param navController the navigation controller
 */
@Composable
fun GameplayMenuScreen(navController: NavController) {
    var myBoard by remember { mutableStateOf(Board.random()) }
    var opponentBoard by remember { mutableStateOf(Board.random()) }
    var selectedCells by remember { mutableStateOf(emptyList<Coordinate>()) }
    var gameConfig by remember { mutableStateOf<GameConfig?>(null) }

    val gameplayNavController = rememberNavController()
    NavHost(
        navController = gameplayNavController,
        startDestination = "gameplay/menu"
    ) {
        composable("gameplay/menu") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                ScreenTitle(title = stringResource(R.string.gameplay_menu_title))
                MenuButton(
                    onClick = { gameplayNavController.navigate("gameplay/new-game") },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
                    iconDescription = stringResource(R.string.gameplay_new_game_button_description),
                    text = stringResource(id = R.string.gameplay_new_game_button_text)
                )
                MenuButton(
                    onClick = { gameplayNavController.navigate("gameplay/search-game") },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_round_search_24),
                    iconDescription = stringResource(R.string.gameplay_search_game_button_description),
                    text = stringResource(id = R.string.gameplay_search_game_button_text)
                )
                GoBackButton(navController)
            }
        }
        composable("gameplay/new-game") {
            NewGameScreen(
                gameplayNavController,
                onGameConfigured = {
                    gameConfig = it

                    // Api call to add game to lobby
                    opponentBoard = Board.random(it.gridSize)

                    gameplayNavController.navigate("gameplay/board-setup")
                }
            )
        }
        composable("gameplay/search-game") {
            SearchGameScreen(gameplayNavController)
        }
        composable("gameplay/board-setup") {
            gameConfig?.let {
                BoardSetupScreen(
                    gameplayNavController,
                    boardSize = it.gridSize,
                    onBoardSetupFinished = { board ->
                        myBoard = board
                        gameplayNavController.navigate("gameplay/game")
                    }
                )
            }
        }
        composable("gameplay/game") {
            GameplayScreen(
                gameplayNavController,
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
                    // Api call shoot on coordinates

                    // When response is received
                    selectedCells = emptyList()
                },
                onResetShotsButtonPressed = {
                    selectedCells = emptyList()
                }
            )
        }
    }
}
