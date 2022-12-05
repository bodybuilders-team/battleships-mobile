package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.PlayerInfo
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGameCause.DESTRUCTION
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGameCause.RESIGNATION
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

/**
 * The cause of the end of the game.
 *
 * @property DESTRUCTION the game ended because a player's fleet was destroyed
 * @property RESIGNATION the game ended because a player resigned
 */
enum class EndGameCause {
    DESTRUCTION,
    RESIGNATION
}

private const val POPUP_BEHIND_DARKNESS_FACTOR = 0.3f
private const val POPUP_SIZE_FACTOR = 0.5f
private const val POPUP_CORNER_RADIUS = 10
private const val CAUSE_TEXT_FONT_SIZE = 10
private const val CONTENT_WIDTH_FACTOR = 0.8f
private const val WINNING_TEXT_HEIGHT_FACTOR = 0.2f
private const val AVATARS_HEIGHT_FACTOR = 0.6f
private const val AVATAR_HEIGHT_FACTOR = 0.5f
private const val AVATAR_WIDTH_FACTOR = 0.5f
private const val BETWEEN_AVATAR_PADDING = 10

/**
 * The end game popup.
 *
 * @param won true if the player won the game, false otherwise
 * @param cause the cause of the end of the game
 * @param pointsWon the points won by the player
 * @param playerInfo the player's info
 * @param opponentInfo the opponent's info
 * @param onPlayAgainButtonClicked the callback to be invoked when the play again button is clicked
 * @param onBackToMenuButtonClicked the callback to be invoked when the back to menu button is clicked
 */
@Composable
fun EndGamePopUp(
    won: Boolean,
    cause: EndGameCause,
    pointsWon: Int,
    playerInfo: PlayerInfo,
    opponentInfo: PlayerInfo,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    Popup {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = POPUP_BEHIND_DARKNESS_FACTOR)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(POPUP_SIZE_FACTOR)
                    .clip(RoundedCornerShape(POPUP_CORNER_RADIUS.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(CONTENT_WIDTH_FACTOR)
                        .fillMaxHeight(WINNING_TEXT_HEIGHT_FACTOR),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            if (won) R.string.endgame_youWon_text
                            else R.string.endgame_youLost_text
                        ),
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = stringResource(
                            when (cause) {
                                DESTRUCTION -> R.string.endgame_byFleetDestruction_text
                                RESIGNATION -> R.string.endgame_byResignation_text
                            }
                        ),
                        style = MaterialTheme.typography.h6,
                        fontSize = CAUSE_TEXT_FONT_SIZE.sp
                    )
                    Text(
                        text = "+$pointsWon ${stringResource(R.string.endgame_points_text)}",
                        style = MaterialTheme.typography.h6
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(CONTENT_WIDTH_FACTOR)
                        .fillMaxHeight(AVATARS_HEIGHT_FACTOR)
                        .padding(top = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = BETWEEN_AVATAR_PADDING.dp)
                            .fillMaxWidth(AVATAR_WIDTH_FACTOR),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                if (won) playerInfo.avatarId
                                else opponentInfo.avatarId
                            ),
                            contentDescription = stringResource(R.string.endgame_winnersAvatar_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(AVATAR_HEIGHT_FACTOR)
                                .border(1.dp, Color.Black)
                        )
                        Text(
                            text = if (won) playerInfo.name else opponentInfo.name,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                if (won) opponentInfo.avatarId
                                else playerInfo.avatarId
                            ),
                            contentDescription = stringResource(R.string.endgame_losersAvatar_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(AVATAR_HEIGHT_FACTOR)
                                .border(1.dp, Color.Black)
                        )
                        Text(
                            text = if (won) opponentInfo.name else playerInfo.name,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        IconButton(
                            onClick = onPlayAgainButtonClicked,
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_refresh_24),
                            contentDescription = stringResource(R.string.endgame_playAgainButton_description),
                            text = stringResource(R.string.endgame_playAgainButton_text)
                        )

                        IconButton(
                            onClick = onBackToMenuButtonClicked,
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_home_24),
                            contentDescription = stringResource(R.string.endgame_mainMenuButton_description),
                            text = stringResource(R.string.endgame_mainMenuButton_text)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EndGamePopUpPreview() {
    BattleshipsScreen {
        EndGamePopUp(
            won = true,
            cause = DESTRUCTION,
            pointsWon = 100,
            playerInfo = PlayerInfo("Nyck", R.drawable.author_nyckollas_brandao),
            opponentInfo = PlayerInfo("Jesus", R.drawable.author_andre_jesus),
            onPlayAgainButtonClicked = {},
            onBackToMenuButtonClicked = {}
        )
    }
}
