package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.games.models.games.GameStateModel
import pt.isel.pdm.battleships.services.games.models.games.PlayerModel
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutputModel
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutputModel
import pt.isel.pdm.battleships.services.utils.siren.Action
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.services.utils.siren.Link
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.components.GameCard
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.net.URI

/**
 * Lobby screen.
 *
 * @param state the current state of the lobby
 * @param games the list of games
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun LobbyScreen(
    state: LobbyState,
    games: GetGamesOutput?,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(R.string.lobby_title))

            when (state) {
                FINISHED ->
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
                            val game =
                                games.entities?.get(index) as EmbeddedSubEntity<GetGameOutputModel>
                            GameCard(
                                game = game,
                                onGameInfoRequest = { /*TODO*/ },
                                onJoinGameRequest = { /*TODO*/ }
                            )
                        }
                    }
                else -> LoadingSpinner()
            }

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}

@Preview
@Composable
private fun LobbyScreenPreview() {
    LobbyScreen(
        state = FINISHED,
        games = GetGamesOutput(
            properties = GetGamesOutputModel(1),
            entities = listOf(
                EmbeddedSubEntity(
                    rel = listOf(Rels.ITEM, Rels.GAME, "${Rels.GAME}-1"),
                    properties = GetGameOutputModel(
                        id = 1,
                        name = "Game 1",
                        creator = "joe",
                        config = GameConfigModel(
                            gridSize = 10,
                            maxTimeForLayoutPhase = 10,
                            shotsPerRound = 10,
                            maxTimePerRound = 10,
                            shipTypes = emptyList()
                        ),
                        state = GameStateModel(
                            "WAITING_FOR_PLAYERS",
                            0,
                            null,
                            null,
                            null
                        ),
                        players = listOf(PlayerModel("joe", 0))
                    ),
                    links = listOf(
                        Link(
                            rel = listOf(Rels.SELF),
                            href = URI.create("games/1")
                        )
                    ),
                    actions = listOf(
                        Action(
                            name = Rels.JOIN_GAME,
                            title = "Join Game",
                            method = "POST",
                            href = URI("games/1/join")
                        )
                    )
                )
            )
        ),
        onBackButtonClicked = { }
    )
}

@Preview
@Composable
private fun EmptyLobbyScreenPreview() {
    LobbyScreen(
        state = FINISHED,
        games = GetGamesOutput(
            properties = GetGamesOutputModel(0),
            entities = listOf()
        ),
        onBackButtonClicked = { }
    )
}