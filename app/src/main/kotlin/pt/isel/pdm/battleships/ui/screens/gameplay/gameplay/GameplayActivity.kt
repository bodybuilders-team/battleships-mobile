package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayEvent.Exit
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_MY_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the gameplay screen.
 */
class GameplayActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::GameplayViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE) {
            viewModel.updateLinks(intent.getLinks())
            viewModel.loadGame()
        }

        setContent {
            GameplayActivityScreen(
                viewModel = viewModel,
                onPlayAgainButtonClicked = { finish() },
                onBackToMenuButtonClicked = { finish() }
            )
        }
    }

    /**
     * Handles the specified event.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Exit -> finish()
            is Event.Error -> showToast(event.message)
        }
    }
}

/**
 * Gameplay Activity Screen.
 *
 * Appropriately chooses between the different composable screens based on the current state in
 * [viewModel]:
 * - LoadingScreens ([LoadingSpinner]) for both loading the game and loading the fleet;
 * - [GameplayScreen] for the actual gameplay.
 *
 * @param viewModel the view model used to handle the board setup screen
 * @param onPlayAgainButtonClicked the callback to be invoked when the play again button is clicked
 * @param onBackToMenuButtonClicked the callback to be invoked when the back to menu button is clicked
 */
@Composable
private fun GameplayActivityScreen(
    viewModel: GameplayViewModel,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    val gameState = viewModel.screenState.gameState
        ?: throw IllegalStateException("Game state not found")
    val player = viewModel.screenState.player
        ?: throw IllegalStateException("Player not found")
    val opponent = viewModel.screenState.opponent
        ?: throw IllegalStateException("Opponent not found")

    BattleshipsScreen {
        when (viewModel.state) {
            IDLE, LINKS_LOADED, LOADING_GAME -> LoadingSpinner("Loading Game...")
            GAME_LOADED, LOADING_MY_FLEET -> LoadingSpinner("Loading Fleet...")
            else -> GameplayScreen(
                myTurn = viewModel.screenState.myTurn
                    ?: throw IllegalStateException("My turn not found"),
                myBoard = viewModel.screenState.myBoard
                    ?: throw IllegalStateException("My board not found"),
                opponentBoard = viewModel.screenState.opponentBoard
                    ?: throw IllegalStateException("Opponent board not found"),
                gameConfig = viewModel.screenState.gameConfig
                    ?: throw IllegalStateException("Game config not found"),
                gameState = gameState,
                onShootClicked = { coordinates -> viewModel.fireShots(coordinates) },
                onLeaveGameButtonClicked = { viewModel.leaveGame() }
            )
        }
        if (viewModel.state == GameplayViewModel.GameplayState.FINISHED_GAME) {
            EndGamePopUp(
                winningPlayer = if (gameState.winner == player.name) WinningPlayer.YOU
                else WinningPlayer.OPPONENT,
                cause = gameState.endCause
                    ?: throw IllegalStateException("End cause not found but game is finished"),
                player = player,
                opponent = opponent,
                onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                onBackToMenuButtonClicked = onBackToMenuButtonClicked
            )
        }
    }
}
