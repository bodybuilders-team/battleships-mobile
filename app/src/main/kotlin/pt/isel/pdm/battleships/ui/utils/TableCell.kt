package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private const val TABLE_BORDER_WIDTH = 1
private const val TABLE_CELL_WIDTH = 100
private const val TABLE_CELL_HEIGHT = 30

/**
 * Table cell that displays a text.
 *
 * @param modifier modifier to be applied to the cell
 * @param text text to be displayed
 * @param textColor color of the text
 * @param textModifier modifier to be applied to the text
 */
@Composable
fun TableCell(
    modifier: Modifier = Modifier,
    text: String,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified
) {
    Box(
        modifier = Modifier
            .width(TABLE_CELL_WIDTH.dp)
            .height(TABLE_CELL_HEIGHT.dp)
            .border(TABLE_BORDER_WIDTH.dp, Color.Black)
            .then(modifier)
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = textModifier.align(Alignment.Center)
        )
    }
}
