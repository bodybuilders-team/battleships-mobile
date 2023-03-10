package pt.isel.pdm.battleships.ui.screens.gameplay.createGame

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.IDLE
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the game configuration screen.
 *
 * @property viewModel the view model for the game configuration screen
 */
class CreateGameActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::CreateGameViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE)
            viewModel.updateLinks(intent.getLinks())

        setContent {
            CreateGameScreen(
                state = viewModel.state,
                onGameConfigured = { gameName, gameConfig ->
                    viewModel.createGame(name = gameName, config = gameConfig)
                },
                onBackButtonClicked = { finish() }
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
            is CreateGameViewModel.CreateGameEvent.NavigateToBoardSetup -> {
                navigateWithLinksTo<BoardSetupActivity>(viewModel.getLinks())
                finish()
            }
            is Event.Error -> showToast(event.message)
        }
    }
}
