package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGameCause
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.EndGamePopUp
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.OpponentBoardView
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.Round
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components.Timer
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.BoardViewWithIdentifiers
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.TileHitView
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.board.getTileSize
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.PlacedShipView
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.IconButton
import java.time.Instant

const val SMALLER_BOARD_TILE_SIZE_FACTOR = 0.5f

data class PlayerInfo(
    val name: String,
    val avatarId: Int
)

/**
 * The gameplay screen.
 *
 * @param myBoard the player's board
 * @param gameConfig the game configuration
 * @param onShootClicked the callback to be invoked when the player shoots
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked
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
    onBackButtonClicked: () -> Unit,
    onPlayAgainButtonClicked: () -> Unit,
    onBackToMenuButtonClicked: () -> Unit
) {
    var selectedCells by remember { mutableStateOf(listOf<Coordinate>()) }

    var canFireShots by remember { mutableStateOf(myTurn) }

    var timerMinutes by remember {
        mutableStateOf(
            (
                Instant.ofEpochMilli(gameState.phaseEndTime)
                    .minusMillis(Instant.now().toEpochMilli())
                    .epochSecond / 60
                ).toInt()
        )
    }
    var timerSeconds by remember {
        mutableStateOf(
            (
                Instant.ofEpochMilli(gameState.phaseEndTime)
                    .minusMillis(Instant.now().toEpochMilli())
                    .epochSecond % 60
                ).toInt()
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)

            if (timerSeconds != 0) {
                timerSeconds--
            } else if (timerMinutes != 0) {
                timerSeconds = 59
                timerMinutes--
            }
        }
    }

    LaunchedEffect(myTurn) {
        if (myTurn)
            canFireShots = true
    }

    val myBoardComposable = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.gameplay_myBoard_description))

            BoardViewWithIdentifiers(
                board = myBoard,
                tileSizeFactor = if (!myTurn) 1.0f else SMALLER_BOARD_TILE_SIZE_FACTOR
            ) {
                val tileSize = getTileSize(myBoard.size) *
                    if (!myTurn) 1.0f
                    else SMALLER_BOARD_TILE_SIZE_FACTOR

                myBoard.fleet.forEach { ship ->
                    PlacedShipView(
                        ship = ship,
                        tileSize = tileSize
                    )
                }

                Row {
                    repeat(opponentBoard.size) { colIdx ->
                        Column {
                            repeat(opponentBoard.size) { rowIdx ->
                                val coordinate = Coordinate(
                                    col = Board.FIRST_COL + colIdx,
                                    row = Board.FIRST_ROW + rowIdx
                                )

                                Box(
                                    modifier = Modifier
                                        .size(tileSize.dp)
                                ) {
                                    val cell = myBoard.getCell(coordinate)
                                    if (cell.wasHit) {
                                        TileHitView(
                                            tileSize = tileSize,
                                            hitShip = cell is ShipCell
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val opponentBoardComposable = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.gameplay_opponentBoard_description))

            OpponentBoardView(
                opponentBoard = opponentBoard,
                myTurn = myTurn,
                selectedCells = selectedCells,
                onTileClicked = { coordinate ->
                    if (!canFireShots || opponentBoard.getCell(coordinate).wasHit)
                        return@OpponentBoardView

                    selectedCells = when {
                        coordinate in selectedCells -> selectedCells - coordinate
                        selectedCells.size < gameConfig.shotsPerTurn ->
                            selectedCells + coordinate
                        gameConfig.shotsPerTurn == 1 -> listOf(coordinate)
                        else -> selectedCells
                    }
                }
            )
        }
    }

    BattleshipsScreen {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 2.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Timer(minutes = timerMinutes, seconds = timerSeconds)
                    Round(
                        round = gameState.round ?: throw IllegalStateException("Round is null")
                    )
                }

                if (myTurn) {
                    opponentBoardComposable()
                } else {
                    myBoardComposable()
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (myTurn) {
                        myBoardComposable()
                    } else {
                        opponentBoardComposable()
                    }

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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_missile),
                                contentDescription = stringResource(
                                    R.string.gameplay_shoot_button_description
                                ),
                                text = stringResource(R.string.gameplay_shoot_buttonText)
                            )
                            IconButton(
                                onClick = { selectedCells = emptyList() },
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_round_refresh_24
                                ),
                                contentDescription = stringResource(
                                    R.string.gameplay_resetShotsButton_description
                                ),
                                text = stringResource(R.string.gameplay_resetShotsButton_text)
                            )
                        }
                    }
                }

                GoBackButton(onClick = onBackButtonClicked)

                if (gameState.winner != null) {
                    EndGamePopUp(
                        won = !myTurn,
                        cause = EndGameCause.DESTRUCTION,
                        pointsWon = 100,
                        playerInfo = playerInfo,
                        opponentInfo = opponentInfo,
                        onPlayAgainButtonClicked = onPlayAgainButtonClicked,
                        onBackToMenuButtonClicked = onBackToMenuButtonClicked
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameplayScreenPreview() {
    GameplayScreen(
        myTurn = true,
        myBoard = MyBoard(),
        opponentBoard = OpponentBoard(),
        gameConfig = GameConfig(10, 1, 30, 30, ShipType.defaultsMap),
        gameState = GameState(
            phase = "",
            phaseEndTime = Instant.now().plusMillis(30000L).toEpochMilli(),
            round = 1,
            turn = "Jesus",
            winner = null
        ),
        playerInfo = PlayerInfo("Jesus", R.drawable.andre_jesus),
        opponentInfo = PlayerInfo("Nyck", R.drawable.nyckollas_brandao),
        onShootClicked = { },
        onBackButtonClicked = { },
        onPlayAgainButtonClicked = { },
        onBackToMenuButtonClicked = { }
    )
}

@Preview
@Composable
private fun GameplayScreenGameEndedPreview() {
    GameplayScreen(
        myTurn = false,
        myBoard = MyBoard(),
        opponentBoard = OpponentBoard(),
        gameConfig = GameConfig(10, 1, 30, 30, ShipType.defaultsMap),
        gameState = GameState(
            phase = "",
            phaseEndTime = Instant.now().plusMillis(30000L).toEpochMilli(),
            round = 1,
            turn = "Jesus",
            winner = "Jesus"
        ),
        playerInfo = PlayerInfo("Jesus", R.drawable.andre_jesus),
        opponentInfo = PlayerInfo("Nyck", R.drawable.nyckollas_brandao),
        onShootClicked = { },
        onBackButtonClicked = { },
        onPlayAgainButtonClicked = { },
        onBackToMenuButtonClicked = { }
    )
}
