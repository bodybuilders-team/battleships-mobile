package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuEvent
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuLoadingState
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeActivity
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.ToastDuration
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the gameplay menu screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property viewModel the view model used to handle the gameplay menu screen
 */
class GameplayMenuActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    val viewModel by viewModelInit {
        GameplayMenuViewModel(
            usersService = battleshipsService.usersService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.links = intent.getLinks().links

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        setContent {
            GameplayMenuScreen(
                viewModel.loadingState,
                onMatchmakeClick = {
                    viewModel.navigateTo<MatchmakeActivity>(setOf(Rels.MATCHMAKE))
                },
                onCreateGameClick = {
                    viewModel.navigateTo<GameConfigurationActivity>(setOf(Rels.CREATE_GAME))
                },
                onLobbyClick = {
                    viewModel.navigateTo<LobbyActivity>(setOf(Rels.LIST_GAMES))
                },
                onBackButtonClick = { finish() }
            )
        }
    }

    /**
     * Handles the events emitted by the view model.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is GameplayMenuEvent.Navigate -> {
                val rels = event.linkRels
                    ?: throw IllegalStateException("No links provided for navigation")

                val links = viewModel.links
                    .filter { it.key in rels }

                navigateWithLinksTo(event.clazz, links)
                viewModel.loadingState = GameplayMenuLoadingState.NOT_LOADING
            }
            is Event.Error -> showToast(event.message, ToastDuration.LONG)
        }
    }
}
