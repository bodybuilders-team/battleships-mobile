package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.Rels.MATCHMAKE
import pt.isel.pdm.battleships.utils.navigateTo
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * Activity for the quick play screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonEncoder the json formatter
 * @property viewModel the view model used to handle the quick play process
 */
class MatchmakeActivity : ComponentActivity() {

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
        MatchmakeViewModel(
            sessionManager = sessionManager,
            gamesService = battleshipsService.gamesService,
            jsonEncoder = jsonEncoder,
            assetManager = assets
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()
        val matchmakeLink =
            links[MATCHMAKE]
                ?: throw IllegalArgumentException("Missing $MATCHMAKE link")

        viewModel.matchmake(
            matchmakeLink
        )

        setContent {
            val state = viewModel.state
            LaunchedEffect(state) {
                if (state == MatchmakeViewModel.MatchmakeState.MATCHMADE) {
                    navigateTo<BoardSetupActivity> {
                        it.putExtra("gameLink", viewModel.gameLink)
                    }
                }
            }

            MatchmakeScreen(
                state = viewModel.state,
                errorMessage = viewModel.errorMessage
            )
        }
    }
}
