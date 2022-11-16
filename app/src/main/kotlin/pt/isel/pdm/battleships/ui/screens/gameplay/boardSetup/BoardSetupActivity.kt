package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayActivity
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the board setup screen.
 *
 * @property viewModel the view model used to handle the board setup screen
 */
class BoardSetupActivity : ComponentActivity() {

    val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModelInit {
        BoardSetupViewModel(
            battleshipsService = dependenciesContainer.battleshipsService,
            sessionManager = dependenciesContainer.sessionManager
        )
    }

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
                IDLE, LINKS_LOADED, LOADING_GAME -> {
                    Text("Loading Game..")
                }
                WAITING_FOR_OPPONENT -> {
                    Text("Waiting for opponent..")
                }
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
