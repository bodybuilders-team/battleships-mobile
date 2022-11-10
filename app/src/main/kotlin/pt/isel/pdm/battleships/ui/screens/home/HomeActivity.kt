package pt.isel.pdm.battleships.ui.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.about.AboutActivity
import pt.isel.pdm.battleships.ui.screens.authentication.login.LoginActivity
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuActivity
import pt.isel.pdm.battleships.ui.screens.ranking.RankingActivity
import pt.isel.pdm.battleships.utils.Links
import pt.isel.pdm.battleships.utils.Links.Companion.LINKS_KEY
import pt.isel.pdm.battleships.utils.navigateTo
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadHome()

        setContent {
            HomeScreen(
                loggedIn = sessionManager.isLoggedIn(),
                onGameplayMenuClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_GAMEPLAY_MENU
                    navigateWithActionTo<GameplayMenuActivity>(
                        linkKeys = setOf("create-game", "list-games", "matchmake")
                    )
                },
                onLoginClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_LOGIN
                    navigateWithActionTo<LoginActivity>(
                        linkKeys = setOf("login")
                    )
                },
                onRegisterClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_REGISTER
                    navigateWithActionTo<RegisterActivity>(
                        linkKeys = setOf("register")
                    )
                },
                onLogoutClick = {
                    sessionManager.clearSession()
                },
                onRankingClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_RANKING
                    navigateWithActionTo<RankingActivity>(
                        linkKeys = setOf("list-users")
                    )
                },
                onAboutClick = {
                    viewModel.loadingState = HomeViewModel.LoadingState.LOADING_ABOUT
                    navigateWithActionTo<AboutActivity>()
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
            viewModel.loadingState = HomeViewModel.LoadingState.NOT_LOADING
        }
    }
}
