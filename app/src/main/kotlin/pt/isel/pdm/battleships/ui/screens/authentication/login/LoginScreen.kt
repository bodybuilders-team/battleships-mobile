package pt.isel.pdm.battleships.ui.screens.authentication

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
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle
import pt.isel.pdm.battleships.viewModels.authentication.AuthenticationState
import pt.isel.pdm.battleships.viewModels.authentication.LoginViewModel

/**
 * Screen for login operation
 *
 * @param vm login view model
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun LoginScreen(
    vm: LoginViewModel,
    onBackButtonClicked: () -> Unit
) {
    val loginMessage = remember { mutableStateOf<String?>(null) }

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authenticationMessageInvalidUsername =
        stringResource(id = R.string.authentication_message_invalid_username)
    val authenticationMessageInvalidPassword =
        stringResource(id = R.string.authentication_message_invalid_password)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.login_title))

        LoginTextFields(
            username = username.value,
            password = password.value,
            onUsernameChangeCallback = { username.value = it },
            onPasswordChangeCallback = { password.value = it }
        )

        LoginButton(enabled = vm.state != AuthenticationState.LOADING) {
            validateLoginFields(
                username = username.value,
                password = password.value,
                invalidUsernameMessage = authenticationMessageInvalidUsername,
                invalidPasswordMessage = authenticationMessageInvalidPassword
            )?.let {
                loginMessage.value = it
                return@LoginButton
            }

            val hashedPassword = hash(password.value)

            vm.login(
                username = username.value,
                password = hashedPassword
            )
        }

        loginMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        if (vm.state == AuthenticationState.SUCCESS) {
            onBackButtonClicked()
        } else if (vm.state == AuthenticationState.ERROR) {
            loginMessage.value = vm.errorMessage
        }

        GoBackButton(
            onClick = onBackButtonClicked
        )
    }
}
