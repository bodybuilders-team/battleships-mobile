package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.getLinks
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.GameConfigurationScreen
import pt.isel.pdm.battleships.viewModels.gameplay.GameConfigurationViewModel

/**
 * Activity for the new game screen.
 */
class GameConfigurationActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val viewModel by viewModelInit { GameConfigurationViewModel(
        battleshipsService.gamesService
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        setContent {
            GameConfigurationScreen(
                viewModel.state,
                onGameConfigured = { gameConfig ->
                    viewModel.createGame(
                        links["create-game"]
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
