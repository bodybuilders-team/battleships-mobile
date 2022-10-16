package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel.LobbyState

/**
 * Screen that displays the lobby menu.
 *
 * @param vm search game view model
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun LobbyScreen(
    vm: LobbyViewModel,
    onBackButtonClicked: () -> Unit
) {
    LaunchedEffect(Unit) {
        vm.getAllGames()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(id = R.string.lobby_title))

        when (vm.state) {
            LobbyState.GETTING_GAMES -> Text(text = "Searching...")
            LobbyState.FINISHED ->
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    val games = vm.games ?: throw IllegalStateException("Games should not be null")
                    val totalCount = games.properties?.totalCount ?: 0

                    items(totalCount) { index ->
                        val game = games.entities?.get(index) as EmbeddedLink
                        GameCard(
                            game = game,
                            onGameInfoRequest = { /*TODO*/ },
                            onJoinGameRequest = { /*TODO*/ }
                        )
                    }
                }
            LobbyState.ERROR -> Text(text = "Error" + vm.errorMessage)
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}
