package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

/**
 * Represents a button in the main menu.
 *
 * @param onClick the action to be performed when the button is clicked
 * @param icon the icon of the button
 * @param iconDescription the description of the icon
 * @param text the text of the button
 */
@Composable
fun MenuButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconDescription: String,
    text: String
) {
    IconButton(
        onClick = onClick,
        icon = icon,
        iconDescription = iconDescription,
        text = text,
        modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
    )
}
