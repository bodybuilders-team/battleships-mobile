package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.getLinks
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.gameplay.QuickPlayScreen
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel

/**
 * Activity for the quick play screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonFormatter the json formatter
 * @property viewModel the view model used to handle the quick play process
 */
class QuickPlayActivity : ComponentActivity() {

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
        QuickPlayViewModel(
            sessionManager = sessionManager,
            gamesService = battleshipsService.gamesService,
            jsonFormatter = jsonFormatter,
            assetManager = assets
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        viewModel.matchmake(
            links["matchmake"]
                ?: throw IllegalArgumentException("Missing matchmake link")
        )

        setContent {
            QuickPlayScreen(
                state = viewModel.state,
                onMatchmade = {
                    navigateTo<BoardSetupActivity> {
                        it.putExtra("gameLink", viewModel.gameLink)
                    }
                },
                errorMessage = viewModel.errorMessage
            )
        }
    }
}
