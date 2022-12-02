package pt.isel.pdm.battleships.ui.utils.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val TABLE_CELL_BORDER_WIDTH = 1
const val TABLE_CELL_WIDTH = 100
const val TABLE_CELL_HEIGHT = 30

/**
 * Table cell that displays a label.
 * Like a [NormalTableCell] but the font weight is bold.
 *
 * @param text text to be displayed
 */
@Composable
fun LabelCell(text: String) {
    TableCell {
        Text(
            text = text,
            color = Color.Unspecified,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Normal table cell displaying a text.
 *
 * @param text text to be displayed
 * @param textColor color of the text
 */
@Composable
fun NormalTableCell(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.Unspecified
) {
    TableCell(modifier = modifier) {
        Text(text = text, color = textColor)
    }
}

/**
 * Table cell that displays a content.
 *
 * @param content the content of the cell
 */
@Composable
private fun TableCell(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(TABLE_CELL_WIDTH.dp)
            .height(TABLE_CELL_HEIGHT.dp)
            .border(TABLE_CELL_BORDER_WIDTH.dp, Color.Black)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
