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
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.service.services.games.models.games.GameConfigModel
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
            text = "Grid Size: ${gameConfig.gridSize}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Max Time for Layout Phase: ${gameConfig.maxTimeForLayoutPhase}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Shots per Round: ${gameConfig.shotsPerRound}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Max Time per Shot: ${gameConfig.maxTimePerRound}",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )

        Row {
            NormalTableCell(text = "Ship Type")
            NormalTableCell(text = "Size")
            NormalTableCell(text = "Quantity")
            NormalTableCell(text = "Points")
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
