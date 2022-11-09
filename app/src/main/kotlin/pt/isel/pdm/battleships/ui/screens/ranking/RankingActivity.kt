package pt.isel.pdm.battleships.ui.screens.ranking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.Rels.LIST_USERS
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * Activity for the ranking screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonEncoder the json formatter
 * @property viewModel the view model used to handle the state of the application
 */
class RankingActivity : ComponentActivity() {

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
        RankingViewModel(
            sessionManager = sessionManager,
            usersService = battleshipsService.usersService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        viewModel.getUsers(
            links[LIST_USERS]
                ?: throw IllegalArgumentException("Missing $LIST_USERS link")
        )

        setContent {
            RankingScreen(
                state = viewModel.state,
                users = viewModel.users,
                errorMessage = viewModel.errorMessage,
                onBackButtonClicked = { finish() }
            )
        }
    }
}
