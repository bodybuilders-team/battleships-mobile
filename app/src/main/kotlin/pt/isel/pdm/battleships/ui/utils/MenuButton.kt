package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private const val BUTTON_PADDING = 6
private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f
private const val BUTTON_CORNER_RADIUS = 8

/**
 * Represents a button in the main menu.
 *
 * @param icon the icon of the button
 * @param iconDescription the description of the icon
 * @param text the text of the button
 * @param onClick the action to be performed when the button is clicked
 */
@Composable
fun MenuButton(
    icon: ImageVector,
    iconDescription: String,
    text: String,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR),
        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription
        )
        Spacer(modifier = Modifier.width(BUTTON_PADDING.dp))
        Text(text = text)
    }
}
