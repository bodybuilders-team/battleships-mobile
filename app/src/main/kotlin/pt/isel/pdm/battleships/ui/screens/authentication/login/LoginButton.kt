package pt.isel.pdm.battleships.ui.screens.authentication.login

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.utils.IconButton

/**
 * Button for login operation
 *
 * @param enabled whether the button is enabled or not
 * @param onLoginClickCallback callback to be invoked when the login button is clicked
 */
@Composable
fun LoginButton(
    enabled: Boolean = true,
    onLoginClickCallback: () -> Unit
) {
    IconButton(
        onClick = onLoginClickCallback,
        enabled = enabled,
        modifier = Modifier.padding(BUTTON_PADDING.dp),
        text = stringResource(id = R.string.login_button_text),
        icon = ImageVector.vectorResource(id = R.drawable.ic_round_login_24),
        iconDescription = stringResource(id = R.string.main_menu_login_button_content_description)
    )
}

private const val BUTTON_PADDING = 8
