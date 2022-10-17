package pt.isel.pdm.battleships.ui.screens.authentication.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.authentication.hash
import pt.isel.pdm.battleships.ui.screens.authentication.validateEmailFields
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle
import pt.isel.pdm.battleships.viewModels.authentication.AuthenticationState
import pt.isel.pdm.battleships.viewModels.authentication.RegisterViewModel

/**
 * Screen for registering a new user
 *
 * @param vm the view model for the register screen
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onBackButtonClicked: () -> Unit
) {
    val registerMessage = remember { mutableStateOf<String?>(null) }

    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authenticationMessageInvalidUsername =
        stringResource(id = R.string.authentication_message_invalid_username)
    val authenticationMessageInvalidEmail =
        stringResource(id = R.string.authentication_message_invalid_email)
    val authenticationMessageInvalidPassword =
        stringResource(id = R.string.authentication_message_invalid_password)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.register_title))

        RegisterTextFields(
            email = email.value,
            username = username.value,
            password = password.value,
            onEmailChangeCallback = { email.value = it },
            onUsernameChangeCallback = { username.value = it },
            onPasswordChangeCallback = { password.value = it }
        )

        RegisterButton(enabled = vm.state != AuthenticationState.LOADING) {
            validateEmailFields(
                email = email.value,
                username = username.value,
                password = password.value,
                invalidEmailMessage = authenticationMessageInvalidEmail,
                invalidUsernameMessage = authenticationMessageInvalidUsername,
                invalidPasswordMessage = authenticationMessageInvalidPassword
            )?.let {
                registerMessage.value = it
                return@RegisterButton
            }

            val hashedPassword = hash(password.value)

            vm.register(
                email = email.value,
                username = username.value,
                password = hashedPassword
            )
        }

        registerMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        if (vm.state == AuthenticationState.SUCCESS) {
            onBackButtonClicked()
        } else if (vm.state == AuthenticationState.ERROR) {
            registerMessage.value = vm.errorMessage
        }

        GoBackButton(onClick = onBackButtonClicked)
    }
}
