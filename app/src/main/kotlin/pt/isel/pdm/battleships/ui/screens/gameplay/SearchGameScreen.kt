package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.viewModels.gameplay.SearchGameViewModel
import pt.isel.pdm.battleships.viewModels.gameplay.SearchGameViewModel.SearchGameState

/**
 * Screen that displays the search game menu.
 *
 * @param vm search game view model
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun SearchGameScreen(
    vm: SearchGameViewModel,
    onBackButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            delay(ANIMATION_DELAY)
            vm.getAllGames()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when (vm.state) {
                SearchGameState.SEARCHING -> Text(text = "Searching...")
                SearchGameState.SEARCH_FINISHED -> LazyColumn {
                    val games = vm.games!!

                    games.properties?.let { props ->
                        items(props.totalCount) { index ->
                            val game = games.entities?.get(index) as EmbeddedLink
                            Text(text = game.title ?: "Game")
                        }
                    }
                }
                SearchGameState.ERROR -> Text(text = "Error" + vm.errorMessage)
            }
        }
    }
}
