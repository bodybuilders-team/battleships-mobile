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
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.service.media.siren.Action
import pt.isel.pdm.battleships.service.media.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.service.media.siren.Link
import pt.isel.pdm.battleships.service.services.games.models.ShipTypeModel
import pt.isel.pdm.battleships.service.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.service.services.games.models.games.GameStateModel
import pt.isel.pdm.battleships.service.services.games.models.games.PlayerModel
import pt.isel.pdm.battleships.service.services.games.models.games.getGame.GetGameOutputModel
import pt.isel.pdm.battleships.service.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.service.services.games.models.games.getGames.GetGamesOutputModel
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.GAMES_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.components.GameCard
import pt.isel.pdm.battleships.ui.screens.shared.components.GoBackButton
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.screens.shared.components.ScreenTitle
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels
import java.net.URI

/**
 * Lobby screen.
 *
 * @param state the current state of the lobby
 * @param games the list of games
 * @param onJoinGameRequest the callback to be invoked when the user requests to join a game
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun LobbyScreen(
    state: LobbyState,
    games: GetGamesOutput?,
    onJoinGameRequest: (String) -> Unit,
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
                GAMES_LOADED ->
                    LazyColumn(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        games
                            ?: throw IllegalStateException(
                                "Games cannot be null when state is FINISHED"
                            )

                        @Suppress("UNCHECKED_CAST")
                        val filteredGames = games.entities
                            ?.filter { game ->
                                (game as EmbeddedSubEntity<GetGameOutputModel>)
                                    .properties?.state?.phase == "WAITING_FOR_PLAYERS"
                            }
                            ?: emptyList()

                        items(filteredGames.size) { index ->
                            @Suppress("UNCHECKED_CAST")
                            val game = filteredGames[index] as EmbeddedSubEntity<GetGameOutputModel>

                            GameCard(
                                game = game,
                                onJoinGameRequest = {
                                    onJoinGameRequest(game.getAction(Rels.JOIN_GAME).href.path)
                                }
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
        state = GAMES_LOADED,
        games = GetGamesOutput(
            properties = GetGamesOutputModel(1),
            entities = List(20) { game ->
                EmbeddedSubEntity(
                    rel = listOf(Rels.ITEM, Rels.GAME, "${Rels.GAME}-$game"),
                    properties = GetGameOutputModel(
                        id = 1,
                        name = "Test Game $game",
                        creator = "joe",
                        config = GameConfigModel(
                            gridSize = 10,
                            maxTimeForLayoutPhase = 10,
                            shotsPerRound = 10,
                            maxTimePerRound = 10,
                            shipTypes = ShipType.defaults.map {
                                ShipTypeModel(
                                    shipName = it.shipName,
                                    size = it.size,
                                    quantity = 1,
                                    points = it.points
                                )
                            }
                        ),
                        state = GameStateModel("WAITING_FOR_PLAYERS", 0, null, null, null),
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
            }
        ),
        onJoinGameRequest = {},
        onBackButtonClicked = { }
    )
}

@Preview
@Composable
private fun EmptyLobbyScreenPreview() {
    LobbyScreen(
        state = GAMES_LOADED,
        games = GetGamesOutput(
            properties = GetGamesOutputModel(0),
            entities = listOf()
        ),
        onJoinGameRequest = {},
        onBackButtonClicked = { }
    )
}
