package pt.isel.pdm.battleships.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import pt.isel.pdm.battleships.domain.Board
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import pt.isel.pdm.battleships.domain.Coordinate
import pt.isel.pdm.battleships.domain.Orientation
import pt.isel.pdm.battleships.domain.Ship
import pt.isel.pdm.battleships.domain.ShipType
import pt.isel.pdm.battleships.domain.WaterCell

const val TILE_SIZE = 35.0f

enum class GameStatus {
    PLACING_SHIPS, FINISHED, RUNNING
}

@Composable
@Preview
fun BoardView() {
    val placedShips: MutableList<Ship> by remember {
        mutableStateOf(mutableListOf())
    }

    var board by remember {
        val matrix = MutableList(Board.BOARD_SIDE_LENGTH * Board.BOARD_SIDE_LENGTH) { index ->
            val row = Board.BOARD_SIDE_LENGTH - index / Board.BOARD_SIDE_LENGTH
            val col = Coordinate.COLS_RANGE.first + index % Board.BOARD_SIDE_LENGTH

            WaterCell(Coordinate(col, row))
        }

        mutableStateOf(Board(matrix))
    }

    val shipTypes by remember {
        mutableStateOf(ShipType.values())
    }

    var currentShipType by remember {
        mutableStateOf(ShipType.values()[0])
    }

    var selectedOrientation by remember {
        mutableStateOf(Orientation.VERTICAL)
    }

    var gameStatus by remember {
        mutableStateOf(GameStatus.PLACING_SHIPS)
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Box(Modifier.width((TILE_SIZE * Board.BOARD_SIDE_LENGTH).dp)) {
            Column {
                repeat(Board.BOARD_SIDE_LENGTH) { y ->
                    Row {
                        repeat(Board.BOARD_SIDE_LENGTH) { x ->
                            Column {

                                Box(
                                    Modifier
                                        .size(TILE_SIZE.dp)
                                        .background(Color.Blue)
                                        .border(1.dp, Color.Cyan)
                                ) {

                                }
                            }
                        }
                    }
                }
                if (gameStatus == GameStatus.PLACING_SHIPS)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height((TILE_SIZE * 5 + 10).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(2.0f)) {

                        }
                        Button(modifier = Modifier.weight(1.0f),
                            onClick = {
                                selectedOrientation =
                                    if (selectedOrientation == Orientation.HORIZONTAL)
                                        Orientation.VERTICAL
                                    else
                                        Orientation.HORIZONTAL
                            }) {
                            Text("Change Orientation")
                        }
                    }
            }

            for (ship in placedShips) {
                val point = ship.position.toPoint()

                Box(
                    Modifier
                        .offset(
                            (point.first * TILE_SIZE).dp,
                            (point.second * TILE_SIZE).dp
                        )
                        .size(
                            (TILE_SIZE * if (ship.orientation == Orientation.HORIZONTAL) ship.type.size else 1).dp,
                            (TILE_SIZE * if (ship.orientation == Orientation.VERTICAL) ship.type.size else 1).dp
                        )
                        .background(Color.Black)
                )
            }

            if (gameStatus == GameStatus.PLACING_SHIPS)
                UnplacedShipView(
                    initialOffset = Offset(
                        if (selectedOrientation == Orientation.VERTICAL) 2 * TILE_SIZE else TILE_SIZE,
                        TILE_SIZE * Board.BOARD_SIDE_LENGTH + (TILE_SIZE * 5 + 10) / 2 -
                                if (selectedOrientation == Orientation.VERTICAL) 2.5f * TILE_SIZE else TILE_SIZE
                    ),
                    orientation = selectedOrientation,
                    size = currentShipType.size,
                    onShipPlacedCallback = { shipCoord ->
                        val coordinateList = mutableListOf<Coordinate>()

                        repeat(currentShipType.size) { i ->
                            coordinateList.add(
                                Coordinate(
                                    shipCoord.col + if (selectedOrientation == Orientation.HORIZONTAL)
                                        i
                                    else 0,
                                    shipCoord.row + if (selectedOrientation == Orientation.VERTICAL)
                                        i else 0
                                )
                            )
                        }

                        val newShip = Ship(currentShipType, coordinateList)

                        val canPlace = board.canPlaceShip(newShip)
                        if (canPlace) {
                            board = board.placeShip(newShip)
                            placedShips += newShip

                            if (shipTypes.size == currentShipType.ordinal + 1)
                                gameStatus = GameStatus.RUNNING
                            else
                                currentShipType = shipTypes[currentShipType.ordinal + 1]
                        }
                        canPlace
                    }
                )
        }
    }
}