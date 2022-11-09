package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.components.GameCard
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

/**
 * Screen that displays the lobby menu.
 *
 * @param state the current state of the lobby
 * @param games the list of games
 * @param errorMessage the error message to be displayed
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun LobbyScreen(
    state: LobbyState,
    games: GamesDTO?,
    errorMessage: String?,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(id = R.string.lobby_title))

            when (state) {
                LobbyState.GETTING_GAMES, LobbyState.IDLE -> Text(text = "Searching...")
                LobbyState.FINISHED ->
                    LazyColumn(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        games
                            ?: throw IllegalStateException(
                                "Games cannot be null when state is FINISHED"
                            )

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
                LobbyState.ERROR -> Text(text = "Error $errorMessage")
            }

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}
