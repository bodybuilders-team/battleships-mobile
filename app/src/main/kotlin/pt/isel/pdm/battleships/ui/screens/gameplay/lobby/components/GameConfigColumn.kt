package pt.isel.pdm.battleships.ui.screens.gameplay.lobby.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.service.services.games.models.ShipTypeModel
import pt.isel.pdm.battleships.service.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.ui.screens.shared.components.LabelCell
import pt.isel.pdm.battleships.ui.screens.shared.components.NormalTableCell

private const val PADDING = 10
private const val SHIP_TYPES_TABLE_HEIGHT = 160

/**
 * Composable that displays the game configuration.
 *
 * @param gameConfig the game configuration to be displayed
 */
@Composable
fun GameConfigColumn(gameConfig: GameConfigModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PADDING.dp)
    ) {
        Text(
            text = "${stringResource(R.string.gameConfig_gridSize_text)}: ${gameConfig.gridSize}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "${stringResource(R.string.gameConfig_timeForGridLayout_text)}: ${gameConfig.maxTimeForLayoutPhase}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "${stringResource(R.string.gameConfig_shotsPerRound_text)}: ${gameConfig.shotsPerRound}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "${stringResource(R.string.gameConfig_timePerRound_text)}: ${gameConfig.maxTimePerRound}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Row {
            LabelCell(text = stringResource(R.string.lobby_gameConfig_shipType_text))
            LabelCell(text = stringResource(R.string.lobby_gameConfig_size_text))
            LabelCell(text = stringResource(R.string.lobby_gameConfig_quantity_text))
            LabelCell(text = stringResource(R.string.lobby_gameConfig_points_text))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(SHIP_TYPES_TABLE_HEIGHT.dp)
        ) {
            items(gameConfig.shipTypes) { shipType ->
                Row {
                    NormalTableCell(text = shipType.shipName)
                    NormalTableCell(text = shipType.size.toString())
                    NormalTableCell(text = shipType.quantity.toString())
                    NormalTableCell(text = shipType.points.toString())
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameConfigColumnPreview() {
    GameConfigColumn(
        gameConfig = GameConfigModel(
            gridSize = 10,
            maxTimeForLayoutPhase = 100,
            shotsPerRound = 1,
            maxTimePerRound = 100,
            shipTypes = listOf(
                ShipTypeModel(
                    shipName = "Carrier",
                    size = 5,
                    quantity = 1,
                    points = 5
                ),
                ShipTypeModel(
                    shipName = "Battleship",
                    size = 4,
                    quantity = 1,
                    points = 4
                ),
                ShipTypeModel(
                    shipName = "Cruiser",
                    size = 3,
                    quantity = 1,
                    points = 3
                ),
                ShipTypeModel(
                    shipName = "Submarine",
                    size = 3,
                    quantity = 1,
                    points = 3
                ),
                ShipTypeModel(
                    shipName = "Destroyer",
                    size = 2,
                    quantity = 1,
                    points = 2
                )
            )
        )
    )
}
