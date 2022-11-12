package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.CREATE_GAME
import pt.isel.pdm.battleships.ui.utils.navigation.navigateTo
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the game configuration screen.
 *
 * @property battleshipsService @property battleshipsService the service used to handle the battleships game
 * @property viewModel the view model for the game configuration screen
 */
class GameConfigurationActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val viewModel by viewModelInit {
        GameConfigurationViewModel(gamesService = battleshipsService.gamesService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()
        val createGameLink = links[CREATE_GAME]
            ?: throw IllegalStateException("No create game link found")

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        setContent {
            GameConfigurationScreen(
                viewModel.state,
                onGameConfigured = { gameConfig ->
                    viewModel.createGame(
                        createGameLink,
                        gameConfig
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
    private suspend fun handleEvent(event: GameConfigurationViewModel.GameConfigurationEvent) =
        when (event) {
            is GameConfigurationViewModel.GameConfigurationEvent.NavigateToBoardSetup -> {
                navigateTo<BoardSetupActivity> {
                    it.putExtra("gameLink", viewModel.gameLink)
                }
                finish()
            }
            is GameConfigurationViewModel.GameConfigurationEvent.Error -> {
                showToast(event.message)
            }
        }
}