package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.R
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
            BattleshipsScreen {
                when (viewModel.state) {
                    IDLE, LINKS_LOADED, LOADING_GAME ->
                        LoadingSpinner(stringResource(id = R.string.gameplay_loadingGame_text))
                    WAITING_FOR_OPPONENT ->
                        LoadingSpinner(stringResource(id = R.string.gameplay_waitingForOpponent_text))
                    else -> {
                        BoardSetupScreen(
                            boardSize = viewModel.screenState.gridSize
                                ?: throw IllegalStateException("No grid size found"),
                            ships = viewModel.screenState.ships
                                ?: throw IllegalStateException("No ships found"),
                            maxTimeForGridLayout = viewModel.screenState.maxTimeForGridLayout
                                ?: throw IllegalStateException("No max time for grid layout found"),
                            onBoardSetupFinished = { board ->
                                viewModel.deployFleet(board.fleet)
                            },
                            onBackButtonClicked = { finish() }
                        )
                    }
                }
            }
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
