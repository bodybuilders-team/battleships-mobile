package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private const val BUTTON_PADDING = 6

/**
 * Represents a button with an icon.
 *
 * @param onClick the action to be performed when the button is clicked
 * @param imageVector the icon of the button
 * @param contentDescription the description of the icon
 * @param modifier the modifier of the button
 * @param text the text of the button
 * @param enabled whether the button is enabled or not
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        enabled = enabled,
        contentPadding = if (text == null) PaddingValues(0.dp) else ButtonDefaults.ContentPadding
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
        if (text != null) {
            Spacer(modifier = Modifier.width(BUTTON_PADDING.dp))
            Text(text = text)
        }
    }
}
