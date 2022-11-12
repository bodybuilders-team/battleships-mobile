package pt.isel.pdm.battleships.ui.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.about.AboutActivity
import pt.isel.pdm.battleships.ui.screens.authentication.login.LoginActivity
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuActivity
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeEvent
import pt.isel.pdm.battleships.ui.screens.ranking.RankingActivity
import pt.isel.pdm.battleships.ui.utils.ToastDuration
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.utils.Links
import pt.isel.pdm.battleships.utils.Links.Companion.LINKS_KEY
import pt.isel.pdm.battleships.utils.navigateWithLinksTo
import pt.isel.pdm.battleships.utils.navigateWithLinksToForResult
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * This activity is the main entry point of the application.
 * It is responsible for creating the main view and the view model.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonEncoder the json formatter used to format the json data
 * @property viewModel the view model used to handle the state of the application
 */
class HomeActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModelInit {
        HomeViewModel(
            battleshipsService = battleshipsService
        )
    }

    private val userHomeForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultIntent = result.data ?: return@registerForActivityResult
        // This callback runs on the main thread

        val links = resultIntent.getParcelableExtra<Links>(LINKS_KEY)
            ?: throw IllegalStateException("Links not found")
        viewModel.links = viewModel.links + links.links
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        viewModel.loadHome()

        setContent {
            HomeScreen(
                loggedIn = sessionManager.isLoggedIn(),
                onGameplayMenuClick = {
                    viewModel.navigateTo<GameplayMenuActivity>(
                        linkRels = setOf("user-home")
                    )
                },
                onLoginClick = {
                    viewModel.navigateTo<LoginActivity>(
                        linkRels = setOf("login")
                    )
                },
                onRegisterClick = {
                    viewModel.navigateTo<RegisterActivity>(
                        linkRels = setOf("register")
                    )
                },
                onLogoutClick = {
                    sessionManager.clearSession()
                },
                onRankingClick = {
                    viewModel.navigateTo<RankingActivity>(
                        linkRels = setOf("list-users")
                    )
                },
                onAboutClick = {
                    viewModel.navigateTo<AboutActivity>()
                },
                loadingState = viewModel.loadingState
            )
        }
    }

    private suspend fun handleEvent(event: HomeEvent) =
        when (event) {
            is HomeEvent.Navigate -> {
                val links =
                    event.linkRels?.let { rels ->
                        viewModel.links
                            .filter { rels.contains(it.key) }
                            .mapValues { it.value }
                    }

                when (event.clazz) {
                    LoginActivity::class.java, RegisterActivity::class.java -> {
                        navigateWithLinksToForResult(userHomeForResult, event.clazz, links)
                    }
                    else -> {
                        navigateWithLinksTo(event.clazz, links)
                    }
                }
                viewModel.loadingState = HomeViewModel.HomeLoadingState.NOT_LOADING
            }
            is HomeEvent.Error -> {
                showToast(event.message, ToastDuration.LONG) {
                    viewModel.loadHome()
                }
            }
        }
}
