package pt.isel.pdm.battleships.ui.screens.ranking

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsActivity
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.showToast

/**
 * Activity for the ranking screen.
 *
 * @property viewModel the view model used to handle the state of the application
 */
class RankingActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::RankingViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE) {
            viewModel.updateLinks(intent.getLinks())
            viewModel.getUsers()
        }

        setContent {
            RankingScreen(
                state = viewModel.state,
                users = viewModel.users,
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
            is Event.Error -> showToast(event.message)
        }
    }
}
