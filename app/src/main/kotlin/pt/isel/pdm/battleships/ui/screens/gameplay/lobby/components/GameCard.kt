package pt.isel.pdm.battleships.ui.screens.gameplay.lobby.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.service.media.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.service.services.games.models.games.getGame.GetGameOutputModel
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

private const val BUTTON_SIZE = 38
private const val CARD_WIDTH_FACTOR = 0.8f
private const val CARD_HEIGHT = 60
private const val CARD_PADDING = 10
private const val CARD_CORNER_RADIUS = 8
private const val CARD_BORDER_WIDTH = 1

/**
 * Composable that displays a game card in the lobby.
 *
 * @param game the game to be displayed
 * @param onJoinGameRequest the callback to be invoked when the join button is clicked
 */
@Composable
fun GameCard(
    game: EmbeddedSubEntity<GetGameOutputModel>,
    onJoinGameRequest: () -> Unit
) {
    val gameProps = game.properties
        ?: throw IllegalStateException("The game entity does not have properties")

    Column(
        modifier = Modifier
            .fillMaxWidth(CARD_WIDTH_FACTOR)
            .padding(bottom = CARD_PADDING.dp)
            .clip(RoundedCornerShape(CARD_CORNER_RADIUS.dp))
            .border(
                width = CARD_BORDER_WIDTH.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(CARD_CORNER_RADIUS.dp)
            )
            .background(Color.LightGray)
    ) {
        var gameInfoExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CARD_HEIGHT.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(
                    text = gameProps.name,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "Created by ${gameProps.creator}",
                    style = MaterialTheme.typography.body2
                )
            }

            IconButton(
                onClick = { gameInfoExpanded = !gameInfoExpanded },
                painter = painterResource(R.drawable.ic_round_info_24),
                contentDescription = stringResource(R.string.lobby_info_iconDescription),
                modifier = Modifier.size(BUTTON_SIZE.dp)
            )

            IconButton(
                onClick = onJoinGameRequest,
                painter = painterResource(R.drawable.ic_round_play_arrow_24),
                contentDescription = stringResource(R.string.lobby_join_iconDescription),
                modifier = Modifier.size(BUTTON_SIZE.dp)
            )
        }

        if (gameInfoExpanded)
            GameConfigColumn(gameProps.config)
    }
}
