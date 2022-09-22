package pt.isel.pdm.battleships.ui.screens.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board

private const val GAME_CONFIG_TITLE_PADDING = 8

@Composable
fun GameConfigMenu() {
    var gridSize by remember { mutableStateOf(Board.DEFAULT_BOARD_SIZE) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.game_config_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(GAME_CONFIG_TITLE_PADDING.dp)
        )

        GridSizeSelector {
            gridSize = it
        }
    }
}

@Composable
@Preview
fun GridSizeSelector(onGridSizeSelected: (Int) -> Unit = {}) {
    var gridSize by remember { mutableStateOf(Board.DEFAULT_BOARD_SIZE) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Grid Size",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "$gridSize x $gridSize",
                style = MaterialTheme.typography.h6
            )
        }
        Slider(
            value = gridSize.toFloat(),
            onValueChange = { gridSize = it.toInt() },
            valueRange = Board.MIN_BOARD_SIZE.toFloat()..Board.MAX_BOARD_SIZE.toFloat()
        )
    }
}
