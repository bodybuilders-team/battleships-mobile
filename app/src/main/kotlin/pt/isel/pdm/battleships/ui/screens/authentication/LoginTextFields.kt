package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

/**
 * The text fields for the login operation on the login page:
 * - Username field
 * - Password field
 *
 * @param username username to show
 * @param password password to show
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun LoginTextFields(
    username: String,
    password: String,
    onUsernameChangeCallback: (String) -> Unit,
    onPasswordChangeCallback: (String) -> Unit
) {
    TextField(
        value = username,
        onValueChange = onUsernameChangeCallback,
        placeholder = {
            Text(text = stringResource(id = R.string.login_username_placeholder_text))
        },
        modifier = Modifier.padding(bottom = USERNAME_PADDING.dp)
    )

    TextField(
        value = password,
        onValueChange = onPasswordChangeCallback,
        placeholder = {
            Text(text = stringResource(id = R.string.login_password_placeholder_text))
        },
        visualTransformation = PasswordVisualTransformation()
    )
}

private const val USERNAME_PADDING = 8
