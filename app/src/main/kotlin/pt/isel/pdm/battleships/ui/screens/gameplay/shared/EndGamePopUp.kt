package pt.isel.pdm.battleships.ui.screens.gameplay.shared

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.game.EndGameCause
import pt.isel.pdm.battleships.domain.games.game.EndGameCause.DESTRUCTION
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer.NONE
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer.OPPONENT
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer.YOU
import pt.isel.pdm.battleships.domain.users.Player
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

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
 * @param winningPlayer the player who won the game
 * @param cause the cause of the end of the game
 * @param player the player's info
 * @param opponent the opponent's info
 * @param onPlayAgainButtonClicked the callback to be invoked when the play again button is clicked
 * @param onBackToMenuButtonClicked the callback to be invoked when the back to menu button is clicked
 */
@Composable
fun EndGamePopUp(
    winningPlayer: WinningPlayer,
    cause: EndGameCause,
    player: Player,
    opponent: Player,
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
                            when (winningPlayer) {
                                NONE -> R.string.endgame_aborted_text
                                YOU -> R.string.endgame_youWon_text
                                OPPONENT -> R.string.endgame_youLost_text
                            }
                        ),
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = stringResource(
                            when (cause) {
                                DESTRUCTION -> R.string.endgame_byFleetDestruction_text
                                EndGameCause.RESIGNATION -> R.string.endgame_byResignation_text
                                EndGameCause.TIMEOUT -> R.string.endgame_byTimeout_text
                            }
                        ),
                        style = MaterialTheme.typography.h6,
                        fontSize = CAUSE_TEXT_FONT_SIZE.sp
                    )
                    Text(
                        text = if (player.points == 0) stringResource(R.string.endgame_noPointsWon_text)
                        else "+${player.points} ${stringResource(R.string.endgame_points_text)}",
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
                                when (winningPlayer) {
                                    YOU, NONE -> player.avatarId
                                    OPPONENT -> opponent.avatarId
                                }
                            ),
                            contentDescription = stringResource(R.string.endgame_winnersAvatar_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(AVATAR_HEIGHT_FACTOR)
                                .border(1.dp, Color.Black)
                        )
                        Text(
                            text = when (winningPlayer) {
                                YOU, NONE -> player.name
                                OPPONENT -> opponent.name
                            },
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                when (winningPlayer) {
                                    YOU, NONE -> opponent.avatarId
                                    OPPONENT -> player.avatarId
                                }
                            ),
                            contentDescription = stringResource(R.string.endgame_losersAvatar_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(AVATAR_HEIGHT_FACTOR)
                                .border(1.dp, Color.Black)
                        )
                        Text(
                            text = when (winningPlayer) {
                                YOU, NONE -> opponent.name
                                OPPONENT -> player.name
                            },
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
                            painter = painterResource(R.drawable.ic_round_refresh_24),
                            contentDescription = stringResource(R.string.endgame_playAgainButton_description),
                            text = stringResource(R.string.endgame_playAgainButton_text)
                        )

                        IconButton(
                            onClick = onBackToMenuButtonClicked,
                            painter = painterResource(R.drawable.ic_round_home_24),
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
            winningPlayer = YOU,
            cause = DESTRUCTION,
            player = Player(name = "Nyck", points = 0, R.drawable.author_nyckollas_brandao),
            opponent = Player(name = "Jesus", points = 0, R.drawable.author_andre_jesus),
            onPlayAgainButtonClicked = {},
            onBackToMenuButtonClicked = {}
        )
    }
}
