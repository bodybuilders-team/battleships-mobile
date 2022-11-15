package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the game configuration screen.
 *
 * @property viewModel the view model for the game configuration screen
 */
class GameConfigurationActivity : ComponentActivity() {

    private val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModelInit {
        GameConfigurationViewModel(
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

        if (viewModel.state == GameConfigurationState.IDLE) {
            viewModel.updateLinks(intent.getLinks())
        }

        setContent {
            GameConfigurationScreen(
                viewModel.state,
                onGameConfigured = { gameConfig ->
                    viewModel.createGame(
                        gameConfig = gameConfig
                    )
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
            is GameConfigurationViewModel.GameConfigurationEvent.NavigateToBoardSetup -> {
                navigateWithLinksTo<BoardSetupActivity>(viewModel.getLinks())
                finish()
            }
            is Event.Error -> showToast(event.message)
        }
    }
}
