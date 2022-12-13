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
import pt.isel.pdm.battleships.domain.games.game.EndGameCause
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.game.GamePhase
import pt.isel.pdm.battleships.domain.games.game.GamePhase.FINISHED
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.game.WinningPlayer
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.Player
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.MyBoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.OpponentBoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.RoundView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.TimerView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.LeaveGameAlert
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.LeaveGameButton
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton
import java.lang.Integer.max
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
 * @param onShootClicked the callback to be invoked when the player shoots
 * @param onLeaveGameButtonClicked the callback to be invoked when the player leaves the game
 */
@Composable
fun GameplayScreen(
    myTurn: Boolean,
    myBoard: MyBoard,
    opponentBoard: OpponentBoard,
    gameConfig: GameConfig,
    gameState: GameState,
    onShootClicked: (List<Coordinate>) -> Unit,
    onLeaveGameButtonClicked: () -> Unit
) {
    var selectedCells by remember { mutableStateOf(listOf<Coordinate>()) }

    var canFireShots by remember { mutableStateOf(myTurn) }

    LaunchedEffect(myTurn) {
        if (myTurn)
            canFireShots = true
    }

    var time by remember {
        mutableStateOf(
            max(
                (gameState.phaseEndTime - System.currentTimeMillis()).toInt(),
                0
            ) / 1000
        )
    }
    LaunchedEffect(gameState.phaseEndTime) {
        while (time > 0) {
            time = max((gameState.phaseEndTime - System.currentTimeMillis()).toInt(), 0) / 1000
            delay(1000)
        }
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
                    selectedCells.size < gameConfig.shotsPerRound -> selectedCells + coordinate
                    gameConfig.shotsPerRound == 1 -> listOf(coordinate)
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
                            enabled = canFireShots && selectedCells.isNotEmpty(),
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
        }
    }
}

@Preview
@Composable
private fun GameplayScreenPreview() {
    BattleshipsScreen {
        GameplayScreen(
            myTurn = true,
            myBoard = MyBoard(),
            opponentBoard = OpponentBoard(),
            gameConfig = GameConfig(10, 1, 30, 30, ShipType.defaultsMap),
            gameState = GameState(
                phase = GamePhase.IN_PROGRESS,
                phaseEndTime = Instant.now().plusMillis(16000L).toEpochMilli(),
                round = 16,
                turn = "Jesus",
                winner = null,
                endCause = null
            ),
            onShootClicked = { },
            onLeaveGameButtonClicked = { }
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
                phase = FINISHED,
                phaseEndTime = Instant.now().plusMillis(5000L).toEpochMilli(),
                round = 1,
                turn = "Opponent",
                winner = "Opponent",
                endCause = EndGameCause.DESTRUCTION
            ),
            onShootClicked = { },
            onLeaveGameButtonClicked = { }
        )
        EndGamePopUp(
            winningPlayer = WinningPlayer.YOU,
            cause = EndGameCause.RESIGNATION,
            player = Player(
                name = "Player",
                avatarId = R.drawable.ic_round_person_24,
                points = 200
            ),
            opponent = Player(
                name = "Opponent",
                avatarId = R.drawable.ic_round_person_24,
                points = 150
            ),
            onPlayAgainButtonClicked = { },
            onBackToMenuButtonClicked = { }
        )
    }
}
