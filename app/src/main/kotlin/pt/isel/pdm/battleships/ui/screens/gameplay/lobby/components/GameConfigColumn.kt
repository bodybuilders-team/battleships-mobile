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
import androidx.compose.ui.unit.sp
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.ui.utils.components.TableCell

private const val PADDING = 10
private const val CONFIG_TITLE_FONT_SIZE = 18
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
            text = "Configuration",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            fontSize = CONFIG_TITLE_FONT_SIZE.sp
        )

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

        Text(
            text = "Ships:",
            fontWeight = MaterialTheme.typography.h6.fontWeight,
            style = MaterialTheme.typography.body1
        )
        Row {
            TableCell(text = "Ship Type")
            TableCell(text = "Size")
            TableCell(text = "Quantity")
            TableCell(text = "Points")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(SHIP_TYPES_TABLE_HEIGHT.dp)
        ) {
            items(gameConfig.shipTypes) { shipType ->
                Row {
                    TableCell(text = shipType.shipName)
                    TableCell(text = shipType.size.toString())
                    TableCell(text = shipType.quantity.toString())
                    TableCell(text = shipType.points.toString())
                }
            }
        }
    }
}
