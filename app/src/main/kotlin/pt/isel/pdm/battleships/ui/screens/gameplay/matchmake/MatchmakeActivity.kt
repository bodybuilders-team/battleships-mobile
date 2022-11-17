package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeEvent
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.IDLE
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.utils.showToast

/**
 * Activity for the [MatchmakeScreen].
 */
class MatchmakeActivity : BattleshipsActivity() {

    private val viewModel by getViewModel { battleshipsService, sessionManager ->
        MatchmakeViewModel(
            battleshipsService = battleshipsService,
            sessionManager = sessionManager,
            jsonEncoder = dependenciesContainer.jsonEncoder,
            assetManager = assets
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
            viewModel.matchmake()
        }

        setContent {
            MatchmakeScreen(state = viewModel.state)
        }
    }

    /**
     * Handles the specified event.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is MatchmakeEvent.NavigateToBoardSetup -> {
                navigateWithLinksTo<BoardSetupActivity>(links = viewModel.getLinks())
                finish()
            }
            is Event.Error -> showToast(event.message)
        }
    }
}
