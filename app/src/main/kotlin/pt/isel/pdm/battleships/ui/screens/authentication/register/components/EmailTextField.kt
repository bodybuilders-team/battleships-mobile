package pt.isel.pdm.battleships.ui.screens.authentication.register.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.authentication.components.AuthenticationTextField
import pt.isel.pdm.battleships.ui.screens.authentication.validateEmail

@Composable
fun EmailTextField(
    email: String,
    onEmailChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val invalidEmailMessage = stringResource(R.string.register_message_invalidEmail)
    val invalidEmail = email.isNotEmpty() && !validateEmail(email)

    AuthenticationTextField(
        label = stringResource(R.string.register_emailTextField_label),
        value = email,
        onValueChange = onEmailChangeCallback,
        modifier = Modifier.fillMaxWidth()
            .then(modifier),
        required = true,
        errorMessage = if (invalidEmail) invalidEmailMessage else null
    )
}
