package pt.isel.pdm.battleships.ui.screens.authentication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.authentication.MAX_PASSWORD_LENGTH
import pt.isel.pdm.battleships.ui.screens.authentication.validatePassword

/**
 * The password text field.
 *
 * @param password password to show
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun PasswordTextField(
    password: String,
    onPasswordChangeCallback: (String) -> Unit
) {
    val invalidPasswordMessage = stringResource(R.string.authentication_message_invalidPassword)
    val invalidPassword = password.isNotEmpty() && !validatePassword(password)

    AuthenticationTextField(
        label = stringResource(id = R.string.authentication_passwordTextField_label),
        value = password,
        onValueChange = onPasswordChangeCallback,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        required = true,
        maxLength = MAX_PASSWORD_LENGTH,
        errorMessage = if (invalidPassword) invalidPasswordMessage else null
    )
}
