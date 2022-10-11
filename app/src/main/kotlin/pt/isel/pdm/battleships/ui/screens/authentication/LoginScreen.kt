package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Job
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle
import pt.isel.pdm.battleships.viewModels.LoginState
import pt.isel.pdm.battleships.viewModels.LoginViewModel

private const val BUTTON_PADDING = 8

@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onBackButtonClicked: () -> Unit
) {
    val loginMessage = remember { mutableStateOf<String?>(null) }

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val loginMessageInvalidUsername = stringResource(id = R.string.login_message_invalid_username)
    val loginMessageInvalidPassword = stringResource(id = R.string.login_message_invalid_password)
    val loginMessageUsernameNotFound =
        stringResource(id = R.string.login_message_username_not_found)
    val loginMessageWrongPassword = stringResource(id = R.string.login_message_wrong_password)
    val loginMessageSuccessful = stringResource(id = R.string.login_message_successful)

    var loginJob by remember { mutableStateOf<Job?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.login_title))

        LoginTextFields(
            username.value,
            password.value,
            onUsernameChangeCallback = { username.value = it },
            onPasswordChangeCallback = { password.value = it }
        )

        LoginButton(enabled = vm.state != LoginState.LOADING) {
            validateFields(
                username.value,
                password.value,
                loginMessageInvalidUsername,
                loginMessageInvalidPassword
            )?.let {
                loginMessage.value = it
                return@LoginButton
            }

            val hashedPassword = hash(password.value)

            loginJob = vm.login(username.value, hashedPassword)
        }

        loginMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        if (vm.state == LoginState.SUCCESS) {
            loginMessage.value = loginMessageSuccessful
            onBackButtonClicked()
        } else if (vm.state == LoginState.ERROR) {
            loginMessage.value = vm.errorMessage
        }

        GoBackButton(
            onClick = onBackButtonClicked
        )
    }
}
