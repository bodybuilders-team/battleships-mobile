package pt.isel.pdm.battleships.ui.screens.authentication.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

private const val BUTTON_PADDING = 8

/**
 * Button for login operation.
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
        text = stringResource(R.string.login_loginButton_text),
        imageVector = ImageVector.vectorResource(R.drawable.ic_round_login_24),
        contentDescription = stringResource(R.string.login_loginButton_description)
    )
}
