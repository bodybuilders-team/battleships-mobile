package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.PlayerInfo
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGameCause
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.LeaveGameAlert
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.LeaveGameButton
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.MyBoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.OpponentBoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.RoundView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.TimerView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.WinningPlayer.OPPONENT
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.WinningPlayer.YOU
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton
import java.time.Instant

const val SMALLER_BOARD_TILE_SIZE_FACTOR = 0.5f

private const val TIMER_ROUND_ROW_WIDTH_FACTOR = 0.9f
private const val TIMER_ROUND_ROW_TOP_PADDING = 2
private const val TIMER_ROUND_ROW_BOTTOM_PADDING = 10

/**
 * The gameplay screen.
 *
 * @param myTurn whether it's the player's turn
 * @param myBoard the player's board
 * @param opponentBoard the opponent's board
 * @param gameConfig the game configuration
 * @param gameState the game state
 * @param playerInfo the player's info
 * @param opponentInfo the opponent's info
 * @param onShootClicked the callback to be invoked when the player shoots
 * @param time the time left to the player
 * @param onLeaveGameButtonClicked the callback to be invoked when the player leaves the game
 * @param onPlayAgainButtonClicked the callback to be invoked when the player wants to play again
 * @param onBackToMenuButtonClicked the callback to be invoked when the player wants to go back to the menu
 */
@Composable
fun GameplayScreen(
    myTurn: Boolean,
    myBoard: MyBoard,
    opponentBoard: OpponentBoard,
    gameConfig: GameConfig,
    gameState: GameState,
    playerInfo: PlayerInfo,
    opponentInfo: PlayerInfo,
    onShootClicked: (List<Coordinate>) -> Unit,
    time: Int,
    onLeaveGameButtonClicked: () -> Unit,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    var selectedCells by remember { mutableStateOf(listOf<Coordinate>()) }

    var canFireShots by remember { mutableStateOf(myTurn) }

    LaunchedEffect(myTurn) {
        if (myTurn)
            canFireShots = true
    }

    val myBoardComposable = @Composable { MyBoardView(myBoard = myBoard, myTurn = myTurn) }
    val opponentBoardComposable = @Composable {
        OpponentBoardView(
            opponentBoard = opponentBoard,
            myTurn = myTurn,
            selectedCells = selectedCells,
            onTileClicked = { coordinate ->
                if (!canFireShots || opponentBoard.getCell(coordinate).wasHit)
                    return@OpponentBoardView

                selectedCells = when {
                    coordinate in selectedCells -> selectedCells - coordinate
                    selectedCells.size < gameConfig.shotsPerTurn -> selectedCells + coordinate
                    gameConfig.shotsPerTurn == 1 -> listOf(coordinate)
                    else -> selectedCells
                }
            }
        )
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(TIMER_ROUND_ROW_WIDTH_FACTOR)
                    .padding(
                        top = TIMER_ROUND_ROW_TOP_PADDING.dp,
                        bottom = TIMER_ROUND_ROW_BOTTOM_PADDING.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimerView(minutes = time / 60, seconds = time % 60)
                RoundView(round = gameState.round ?: throw IllegalStateException("Round is null"))
            }

            if (myTurn)
                opponentBoardComposable()
            else
                myBoardComposable()

            Row(modifier = Modifier.fillMaxWidth()) {
                if (myTurn)
                    myBoardComposable()
                else
                    opponentBoardComposable()

                if (myTurn) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                canFireShots = false

                                val shots = selectedCells
                                selectedCells = emptyList()

                                onShootClicked(shots)
                            },
                            enabled = canFireShots,
                            painter = painterResource(R.drawable.crosshair_white),
                            contentDescription = stringResource(R.string.gameplay_shoot_button_description),
                            iconModifier = Modifier.size(24.dp),
                            text = stringResource(R.string.gameplay_shoot_buttonText)
                        )
                        IconButton(
                            onClick = { selectedCells = emptyList() },
                            enabled = canFireShots,
                            painter = painterResource(R.drawable.ic_round_refresh_24),
                            contentDescription = stringResource(R.string.gameplay_resetShotsButton_description),
                            text = stringResource(R.string.gameplay_resetShotsButton_text)
                        )
                    }
                }
            }

            var leavingGame by remember { mutableStateOf(false) }

            LeaveGameButton(onClick = { leavingGame = true })

            if (leavingGame)
                LeaveGameAlert(
                    onDismissRequest = { leavingGame = false },
                    onLeaveGameButtonClicked = {
                        leavingGame = false
                        onLeaveGameButtonClicked()
                    }
                )

            if (gameState.winner != null)
                EndGamePopUp(
                    winningPlayer = if (gameState.winner == playerInfo.name) YOU else OPPONENT,
                    cause = when {
                        time <= 0 -> EndGameCause.TIMEOUT
                        myBoard.fleetIsSunk || opponentBoard.fleetIsSunk -> // TODO
                            EndGameCause.DESTRUCTION
                        else -> EndGameCause.RESIGNATION
                    },
                    pointsWon = playerInfo.playerPoints,
                    playerInfo = playerInfo,
                    opponentInfo = opponentInfo,
                    onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                    onBackToMenuButtonClicked = onBackToMenuButtonClicked
                )
        }
    }
}

@Preview
@Composable
private fun GameplayScreenPreview() {
    var timer by remember { mutableStateOf(10) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (timer > 0)
                timer -= 1
        }
    }

    BattleshipsScreen {
        GameplayScreen(
            myTurn = true,
            myBoard = MyBoard(),
            opponentBoard = OpponentBoard(),
            gameConfig = GameConfig(10, 1, 30, 30, ShipType.defaultsMap),
            gameState = GameState(
                phase = "",
                phaseEndTime = Instant.now().plusMillis(10000L).toEpochMilli(),
                round = 16,
                turn = "Jesus",
                winner = null
            ),
            playerInfo = PlayerInfo(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 100
            ),
            opponentInfo = PlayerInfo(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 100
            ),
            onShootClicked = { },
            time = timer,
            onLeaveGameButtonClicked = { },
            onPlayAgainButtonClicked = { },
            onBackToMenuButtonClicked = { }
        )
    }
}

@Preview
@Composable
private fun GameplayScreenGameEndedPreview() {
    BattleshipsScreen {
        GameplayScreen(
            myTurn = false,
            myBoard = MyBoard(),
            opponentBoard = OpponentBoard(),
            gameConfig = GameConfig(10, 1, 5, 30, ShipType.defaultsMap),
            gameState = GameState(
                phase = "",
                phaseEndTime = Instant.now().plusMillis(5000L).toEpochMilli(),
                round = 1,
                turn = "Opponent",
                winner = "Opponent"
            ),
            playerInfo = PlayerInfo(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 40
            ),
            opponentInfo = PlayerInfo(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                playerPoints = 150
            ),
            onShootClicked = { },
            time = 0,
            onLeaveGameButtonClicked = { },
            onPlayAgainButtonClicked = { },
            onBackToMenuButtonClicked = { }
        )
    }
}
