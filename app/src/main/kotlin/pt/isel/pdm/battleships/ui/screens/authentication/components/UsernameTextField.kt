package pt.isel.pdm.battleships.ui.screens.authentication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.authentication.MAX_USERNAME_LENGTH
import pt.isel.pdm.battleships.ui.screens.authentication.validateUsername

/**
 * The username text field.
 *
 * @param username username to show
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param modifier modifier to be applied to the text field
 */
@Composable
fun UsernameTextField(
    username: String,
    onUsernameChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val invalidUsernameMessage = stringResource(R.string.authentication_message_invalidUsername)
    val invalidUsername = username.isNotEmpty() && !validateUsername(username)

    AuthenticationTextField(
        label = stringResource(R.string.authentication_usernameTextField_label),
        value = username,
        onValueChange = onUsernameChangeCallback,
        modifier = Modifier.fillMaxWidth().then(modifier),
        required = true,
        maxLength = MAX_USERNAME_LENGTH,
        forbiddenCharacters = listOf('\n'),
        errorMessage = if (invalidUsername) invalidUsernameMessage else null
    )
}
