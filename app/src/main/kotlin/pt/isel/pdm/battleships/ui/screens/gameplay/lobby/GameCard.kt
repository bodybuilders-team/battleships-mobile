package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.utils.IconButton

/**
 * Composable that displays a game card in the lobby.
 *
 * @param game the game to be displayed
 * @param onGameInfoRequest the callback to be invoked when the info button is clicked
 * @param onJoinGameRequest the callback to be invoked when the join button is clicked
 */
@Composable
fun GameCard(
    game: EmbeddedLink,
    onGameInfoRequest: () -> Unit,
    onJoinGameRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(CARD_WIDTH_FACTOR)
            .height(CARD_HEIGHT.dp)
            .padding(bottom = CARD_PADDING_BOTTOM.dp)
            .clip(RoundedCornerShape(CARD_CORNER_RADIUS.dp))
            .border(CARD_BORDER_WIDTH.dp, Color.DarkGray, RoundedCornerShape(CARD_CORNER_RADIUS.dp))
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = game.title ?: "Game",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(SPACER_WIDTH.dp))

        IconButton(
            onClick = onGameInfoRequest,
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_info_24),
            iconDescription = stringResource(id = R.string.join_icon),
            modifier = Modifier.size(BUTTON_SIZE.dp)
        )

        IconButton(
            onClick = onJoinGameRequest,
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_play_arrow_24),
            iconDescription = stringResource(id = R.string.join_icon),
            modifier = Modifier.size(BUTTON_SIZE.dp)
        )
    }
}

private const val BUTTON_SIZE = 38
private const val SPACER_WIDTH = 100
private const val CARD_WIDTH_FACTOR = 0.8f
private const val CARD_HEIGHT = 60
private const val CARD_PADDING_BOTTOM = 10
private const val CARD_CORNER_RADIUS = 8
private const val CARD_BORDER_WIDTH = 1