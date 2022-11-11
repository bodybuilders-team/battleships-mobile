package pt.isel.pdm.battleships.ui.screens.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.about.AboutActivity
import pt.isel.pdm.battleships.ui.screens.authentication.login.LoginActivity
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuActivity
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.Event
import pt.isel.pdm.battleships.ui.screens.ranking.RankingActivity
import pt.isel.pdm.battleships.ui.utils.ToastDuration
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.utils.Links
import pt.isel.pdm.battleships.utils.Links.Companion.LINKS_KEY
import pt.isel.pdm.battleships.utils.navigateTo
import pt.isel.pdm.battleships.utils.navigateToForResult
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

    private val jsonEncoder by lazy {
        (application as DependenciesContainer).jsonEncoder
    }

    private val viewModel by viewModelInit {
        HomeViewModel(
            battleshipsService = battleshipsService,
            sessionManager = sessionManager,
            assetManager = assets,
            jsonEncoder = jsonEncoder
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
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_GAMEPLAY_MENU
                    viewModel.navigateTo<GameplayMenuActivity>(
                        linkRels = setOf("user-home")
                    )
                },
                onLoginClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_LOGIN
                    viewModel.navigateTo<LoginActivity>(
                        linkRels = setOf("login")
                    )
                },
                onRegisterClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_REGISTER
                    viewModel.navigateTo<RegisterActivity>(
                        linkRels = setOf("register")
                    )
                },
                onLogoutClick = {
                    sessionManager.clearSession()
                },
                onRankingClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_RANKING
                    viewModel.navigateTo<RankingActivity>(
                        linkRels = setOf("list-users")
                    )
                },
                onAboutClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_ABOUT
                    viewModel.navigateTo<AboutActivity>()
                },
                loadingState = viewModel.loadingState
            )
        }
    }

    private suspend fun handleEvent(event: Event) =
        when (event) {
            is Event.Navigate -> {
                if (event.clazz == LoginActivity::class.java ||
                    event.clazz == RegisterActivity::class.java
                ) {
                    navigateWithLinksToForResult(userHomeForResult, event.clazz, event.linkRels)
                } else {
                    navigateWithLinksTo(event.clazz, event.linkRels)
                }
            }
            is Event.Error -> {
                showToast(event.message, ToastDuration.LONG) {
                    viewModel.loadHome()
                }
            }
        }

    /**
     * Navigates to the specified activity, with the given link keys.
     *
     * @param clazz the class of the activity to navigate to
     * @param linkKeys the link keys to set before navigating
     */
    private fun navigateWithLinksTo(
        clazz: Class<*>,
        linkKeys: Set<String>? = null
    ) {
        navigateTo(clazz) { intent ->
            if (linkKeys == null) return@navigateTo

            val links =
                viewModel.links
                    .filter { linkKeys.contains(it.key) }
                    .mapValues { it.value }

            intent.putExtra(LINKS_KEY, Links(links))
        }
        viewModel.loadingState = HomeViewModel.LoadingState.NOT_LOADING
    }

    private fun navigateWithLinksToForResult(
        activityResultLauncher: ActivityResultLauncher<Intent?>,
        clazz: Class<*>,
        linkKeys: Set<String>? = null
    ) {
        navigateToForResult(activityResultLauncher, clazz, beforeNavigation = { intent ->
            if (linkKeys == null) return@navigateToForResult

            val links =
                viewModel.links
                    .filter { linkKeys.contains(it.key) }
                    .mapValues { it.value }

            intent.putExtra(LINKS_KEY, Links(links))
        })
        viewModel.loadingState = HomeViewModel.LoadingState.NOT_LOADING
    }
}
