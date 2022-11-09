package pt.isel.pdm.battleships.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.authentication.LoginActivity
import pt.isel.pdm.battleships.activities.authentication.RegisterActivity
import pt.isel.pdm.battleships.activities.gameplay.GameplayMenuActivity
import pt.isel.pdm.battleships.activities.utils.LINKS_KEY
import pt.isel.pdm.battleships.activities.utils.Links
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.HomeScreen
import pt.isel.pdm.battleships.viewModels.HomeViewModel
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState

/**
 * This activity is the main entry point of the application.
 * It is responsible for creating the main view and the view model.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonFormatter the json formatter used to format the json data
 * @property viewModel the view model used to handle the state of the application
 */
class HomeActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val jsonFormatter by lazy {
        (application as DependenciesContainer).jsonFormatter
    }

    private val viewModel by viewModelInit {
        HomeViewModel(
            battleshipsService,
            sessionManager,
            assets,
            jsonFormatter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadHome()

        setContent {
            HomeScreen(
                loggedIn = sessionManager.isLoggedIn(),
                onGameplayMenuClick = {
                    viewModel.loadingState = LoadingState.LOADING_GAMEPLAY_MENU
                    navigateWithActionTo<GameplayMenuActivity>(
                        linkKeys = setOf("create-game", "list-games", "matchmake")
                    )
                },
                onRankingClick = {
                    viewModel.loadingState = LoadingState.LOADING_RANKING
                    navigateWithActionTo<RankingActivity>(
                        linkKeys = setOf("list-users")
                    )
                },
                onAboutClick = {
                    viewModel.loadingState = LoadingState.LOADING_ABOUT
                    navigateWithActionTo<AboutActivity>()
                },
                onLoginClick = {
                    viewModel.loadingState = LoadingState.LOADING_LOGIN
                    navigateWithActionTo<LoginActivity>(
                        linkKeys = setOf("login")
                    )
                },
                onRegisterClick = {
                    viewModel.loadingState = LoadingState.LOADING_REGISTER
                    navigateWithActionTo<RegisterActivity>(
                        linkKeys = setOf("register")
                    )
                },
                onLogoutClick = {
                    sessionManager.clearSession()
                },
                errorMessage = viewModel.errorMessage,
                onErrorMessageDismissed = {
                    viewModel.state = HomeViewModel.HomeState.IDLE
                    viewModel.errorMessage = null
                    viewModel.loadHome()
                },
                loadingState = viewModel.loadingState
            )
        }
    }

    /**
     * Navigates to the specified activity, with the given link keys.
     *
     * @param T the type of the activity to navigate to
     * @param loadingState the loading state to set before navigating
     * @param linkKeys the link keys to set before navigating
     */
    private inline fun <reified T> navigateWithActionTo(
        linkKeys: Set<String>? = null
    ) {
        viewModel.onHomeLoaded {
            if (viewModel.state != HomeViewModel.HomeState.LOADED) {
                return@onHomeLoaded
            }

            navigateTo<T> { intent ->
                if (linkKeys == null) return@navigateTo

                val links =
                    viewModel.actions
                        .filter { linkKeys.contains(it.key) }
                        .mapValues { it.value.href.path }

                intent.putExtra(LINKS_KEY, Links(links))
            }
            viewModel.loadingState = LoadingState.NOT_LOADING
        }
    }
}
