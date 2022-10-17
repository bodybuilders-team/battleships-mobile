package pt.isel.pdm.battleships.ui.screens.authentication.register

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
private const val EMAIL_PADDING = 8

/**
 * The text fields for the register operation on the register page:
 * - Email field
 * - Username field
 * - Password field
 *
 * @param email email to show
 * @param username username to show
 * @param password password to show
 * @param onEmailChangeCallback callback to be invoked when the email text is changed
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun RegisterTextFields(
    email: String,
    username: String,
    password: String,
    onEmailChangeCallback: (String) -> Unit,
    onUsernameChangeCallback: (String) -> Unit,
    onPasswordChangeCallback: (String) -> Unit
) {
    TextField(
        value = email,
        onValueChange = onEmailChangeCallback,
        placeholder = {
            Text(text = stringResource(id = R.string.authentication_email_placeholder_text))
        },
        modifier = Modifier.padding(bottom = EMAIL_PADDING.dp)
    )

    TextField(
        value = username,
        onValueChange = onUsernameChangeCallback,
        placeholder = {
            Text(text = stringResource(id = R.string.authentication_username_placeholder_text))
        },
        modifier = Modifier.padding(bottom = USERNAME_PADDING.dp)
    )

    TextField(
        value = password,
        onValueChange = onPasswordChangeCallback,
        placeholder = {
            Text(text = stringResource(id = R.string.authentication_password_placeholder_text))
        },
        visualTransformation = PasswordVisualTransformation()
    )
}
