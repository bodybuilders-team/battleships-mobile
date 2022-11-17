package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayActivity
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.utils.showToast

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
            when (viewModel.state) {
                IDLE, LINKS_LOADED, LOADING_GAME -> LoadingSpinner("Loading Game...")
                WAITING_FOR_OPPONENT -> LoadingSpinner("Waiting for opponent...")
                else -> {
                    BoardSetupScreen(
                        boardSize = viewModel.screenState.gridSize
                            ?: throw IllegalStateException("No grid size found"),
                        ships = viewModel.screenState.ships
                            ?: throw IllegalStateException("No ships found"),
                        onBoardSetupFinished = { board ->
                            viewModel.deployFleet(board.fleet)
                        },
                        onBackButtonClicked = { finish() }
                    )
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
