package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.users.PlayerInfo
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_MY_FLEET
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
                onGameLeft = { finish() },
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
 * @param onGameLeft the callback to be invoked when the game is left
 * @param onPlayAgainButtonClicked the callback to be invoked when the play again button is clicked
 * @param onBackToMenuButtonClicked the callback to be invoked when the back to menu button is clicked
 */
@Composable
private fun GameplayActivityScreen(
    viewModel: GameplayViewModel,
    onGameLeft: () -> Unit,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
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
                gameState = viewModel.screenState.gameState
                    ?: throw IllegalStateException("Game state not found"),
                playerInfo = PlayerInfo(
                    name = viewModel.screenState.playerName
                        ?: throw IllegalStateException("Player name not found"),
                    avatarId = R.drawable.ic_round_person_24,
                    playerPoints = viewModel.screenState.playerPoints
                        ?: throw IllegalStateException("Player points not found")
                ),
                opponentInfo = PlayerInfo(
                    name = viewModel.screenState.opponentName
                        ?: throw IllegalStateException("Opponent name not found"),
                    avatarId = R.drawable.ic_round_person_24,
                    playerPoints = viewModel.screenState.opponentPoints
                        ?: throw IllegalStateException("Opponent points not found")
                ),
                onShootClicked = { coordinates -> viewModel.fireShots(coordinates) },
                time = viewModel.screenState.time
                    ?: throw IllegalStateException("No time found"),
                onLeaveGameButtonClicked = { viewModel.leaveGame(onGameLeft = onGameLeft) },
                onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                onBackToMenuButtonClicked = onBackToMenuButtonClicked
            )
        }
    }
}
