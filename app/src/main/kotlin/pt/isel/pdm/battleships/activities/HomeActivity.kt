package pt.isel.pdm.battleships.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.authentication.LoginActivity
import pt.isel.pdm.battleships.activities.authentication.RegisterActivity
import pt.isel.pdm.battleships.activities.gameplay.GameplayMenuActivity
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.HomeScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
import pt.isel.pdm.battleships.ui.utils.navigateTo
import pt.isel.pdm.battleships.viewModels.HomeState
import pt.isel.pdm.battleships.viewModels.HomeViewModel
import pt.isel.pdm.battleships.viewModels.RefreshingState

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
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(
                        loggedIn = sessionManager.isLoggedIn(),
                        onGameplayMenuClick = {
                            navigateWithActionTo<GameplayMenuActivity>(
                                RefreshingState.REFRESHING_GAMEPLAY_MENU
                            )
                        },
                        onRankingClick = {
                            navigateWithActionTo<RankingActivity>(
                                RefreshingState.REFRESHING_RANKING
                            )
                        },
                        onAboutClick = {
                            navigateWithActionTo<AboutActivity>(
                                RefreshingState.REFRESHING_ABOUT
                            )
                        },
                        onLoginClick = {
                            navigateWithActionTo<LoginActivity>(
                                RefreshingState.REFRESHING_LOGIN,
                                setOf("login")
                            )
                        },
                        onRegisterClick = {
                            navigateWithActionTo<RegisterActivity>(
                                RefreshingState.REFRESHING_REGISTER,
                                linkKeys = setOf("register")
                            )
                        },
                        refreshingState = viewModel.refreshingState
                    )
                }
            }
        }
    }

    private inline fun <reified T> navigateWithActionTo(
        refreshingState: RefreshingState,
        linkKeys: Set<String>? = null
    ) {
        viewModel.refreshingState = refreshingState
        viewModel.onHomeLoaded {
            if (viewModel.state != HomeState.LOADED) {
                return@onHomeLoaded
            }

            navigateTo<T> { intent ->
                if (linkKeys == null) return@navigateTo

                val links =
                    viewModel.actions.filter {
                        linkKeys.contains(it.key)
                    }.mapValues { it.value.href.path }

                intent.putExtra("links", HashMap(links))
            }
            viewModel.refreshingState = RefreshingState.NOT_REFRESHING
        }
    }
}
