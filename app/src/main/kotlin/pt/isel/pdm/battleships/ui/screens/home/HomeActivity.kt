package pt.isel.pdm.battleships.ui.screens.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.about.AboutActivity
import pt.isel.pdm.battleships.ui.screens.authentication.login.LoginActivity
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuActivity
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeEvent
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.ranking.RankingActivity
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.ToastDuration
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksToForResult
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the [HomeScreen].
 */
class HomeActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::HomeViewModel)

    private val userHomeForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultIntent = result.data ?: return@registerForActivityResult
        // This callback runs on the main thread

        viewModel.updateUserHomeLinks(resultIntent.getLinks())
        viewModel.loadUserHome()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE) {
            viewModel.updateHomeLinks()
            viewModel.loadHome()
        }

        setContent {
            HomeScreen(
                loggedIn = viewModel.isLoggedIn,
                username = viewModel.username,
                onGameplayMenuClick = { viewModel.navigateTo<GameplayMenuActivity>() },
                onLoginClick = { viewModel.navigateTo<LoginActivity>() },
                onRegisterClick = { viewModel.navigateTo<RegisterActivity>() },
                onLogoutClick = { viewModel.logout() },
                onRankingClick = { viewModel.navigateTo<RankingActivity>() },
                onAboutClick = { viewModel.navigateTo<AboutActivity>() },
                loadingState = viewModel.loadingState
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
            is HomeEvent.Navigate -> {
                val links = viewModel.getLinks()

                when (event.clazz) {
                    LoginActivity::class.java, RegisterActivity::class.java ->
                        navigateWithLinksToForResult(
                            activityResultLauncher = userHomeForResult,
                            clazz = event.clazz,
                            links = links
                        )
                    else -> navigateWithLinksTo(clazz = event.clazz, links = links)
                }

                viewModel.setLoadingStateToLoaded()
            }
            is Event.Error -> showToast(event.message, ToastDuration.LONG)
        }
    }
}
