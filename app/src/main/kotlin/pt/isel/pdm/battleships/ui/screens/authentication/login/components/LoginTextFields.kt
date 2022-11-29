package pt.isel.pdm.battleships.ui.screens.authentication.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val USERNAME_PADDING = 8

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
            Text(text = stringResource(R.string.authentication_usernameTextField_placeholderText))
        },
        modifier = Modifier.padding(bottom = USERNAME_PADDING.dp)
    )

    TextField(
        value = password,
        onValueChange = onPasswordChangeCallback,
        placeholder = {
            Text(text = stringResource(R.string.authentication_passwordTextField_placeholderText))
        },
        visualTransformation = PasswordVisualTransformation()
    )
}
