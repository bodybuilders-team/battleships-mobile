package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.Rels.CREATE_GAME
import pt.isel.pdm.battleships.utils.navigateTo
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * Activity for the new game screen.
 */
class GameConfigurationActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val viewModel by viewModelInit {
        GameConfigurationViewModel(gamesService = battleshipsService.gamesService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        setContent {
            GameConfigurationScreen(
                viewModel.state,
                onGameConfigured = { gameConfig ->
                    viewModel.createGame(
                        links[CREATE_GAME]
                            ?: throw IllegalStateException("No create game link found"),
                        gameConfig
                    )
                },
                onGameCreated = {
                    navigateTo<BoardSetupActivity> {
                        it.putExtra("gameLink", viewModel.gameLink)
                    }
                },
                errorMessage = viewModel.errorMessage,
                onBackButtonClicked = { finish() }
            )
        }
    }
}
