package pt.isel.pdm.battleships.ui.screens.gameplay.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.board.Board

@Composable
fun GridSizeSelector(defaultGridSize: Int, onGridSizeSelected: (Int) -> Unit = {}) {
    var gridSize by remember { mutableStateOf(defaultGridSize) }

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.game_config_grid_size_text),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "$gridSize x $gridSize",
                style = MaterialTheme.typography.h6
            )
        }
        Slider(
            modifier = Modifier,
            value = gridSize.toFloat(),
            onValueChange = {
                gridSize = it.toInt()
                onGridSizeSelected(gridSize)
            },
            valueRange = Board.MIN_BOARD_SIZE.toFloat()..Board.MAX_BOARD_SIZE.toFloat()
        )
    }
}
