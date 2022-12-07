package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.users.PlayerInfo
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayActivity
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the board setup screen.
 *
 * @property viewModel the view model used to handle the board setup screen
 */
class BoardSetupActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::BoardSetupViewModel)

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
            BoardSetupActivityScreen(
                viewModel = viewModel,
                onBackButtonClicked = { finish() },
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
            is BoardSetupViewModel.BoardSetupEvent.NavigateToGameplay -> {
                navigateWithLinksTo<GameplayActivity>(viewModel.getLinks())
                finish()
            }
            is Event.Error -> showToast(event.message)
        }
    }
}

/**
 * Board Setup Activity Screen.
 *
 * Appropriately chooses between the different composable screens based on the current state in
 * [viewModel]:
 * - LoadingScreens ([LoadingSpinner]) for both loading the game and waiting for opponent;
 * - [BoardSetupScreen] for the actual board setup.
 *
 * @param viewModel the view model used to handle the board setup screen
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked
 * @param onPlayAgainButtonClicked the callback to be invoked when the play again button is clicked
 * @param onBackToMenuButtonClicked the callback to be invoked when the back to menu button is clicked
 */
@Composable
private fun BoardSetupActivityScreen(
    viewModel: BoardSetupViewModel,
    onBackButtonClicked: () -> Unit,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        when (viewModel.state) {
            IDLE, LINKS_LOADED, LOADING_GAME ->
                LoadingSpinner(stringResource(R.string.gameplay_loadingGame_text))
            WAITING_FOR_OPPONENT ->
                LoadingSpinner(stringResource(R.string.gameplay_waitingForOpponent_text))
            else -> {
                BoardSetupScreen(
                    boardSize = viewModel.screenState.gridSize
                        ?: throw IllegalStateException("No grid size found"),
                    ships = viewModel.screenState.ships
                        ?: throw IllegalStateException("No ships found"),
                    time = viewModel.screenState.time
                        ?: throw IllegalStateException("No max time for grid layout found"),
                    onTimeChanged = { time -> viewModel.changeTime(time) },
                    playerInfo = PlayerInfo(
                        name = viewModel.screenState.playerName
                            ?: throw IllegalStateException("Player name not found"),
                        avatarId = R.drawable.ic_round_person_24,
                        playerPoints = 0
                    ),
                    opponentInfo = PlayerInfo(
                        name = viewModel.screenState.opponentName
                            ?: throw IllegalStateException("Opponent name not found"),
                        avatarId = R.drawable.ic_round_person_24,
                        playerPoints = 0
                    ),
                    onBoardSetupFinished = { board -> viewModel.deployFleet(board.fleet) },
                    onBackButtonClicked = onBackButtonClicked,
                    onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                    onBackToMenuButtonClicked = onBackToMenuButtonClicked
                )
            }
        }
    }
}
