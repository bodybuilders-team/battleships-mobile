package pt.isel.pdm.battleships.ui.screens.authentication.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.SUCCESS
import pt.isel.pdm.battleships.ui.screens.authentication.hash
import pt.isel.pdm.battleships.ui.screens.authentication.login.components.LoginButton
import pt.isel.pdm.battleships.ui.screens.authentication.login.components.LoginTextFields
import pt.isel.pdm.battleships.ui.screens.authentication.validatePassword
import pt.isel.pdm.battleships.ui.screens.authentication.validateUsername
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

/**
 * Login screen.
 *
 * @param state the authentication state
 * @param onLogin callback to be invoked when the login button is clicked
 * @param onLoginSuccessful callback to be invoked when the login process is successful
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun LoginScreen(
    state: AuthenticationState,
    onLogin: (String, String) -> Unit,
    onLoginSuccessful: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val loginMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state) {
        if (state == SUCCESS) {
            onLoginSuccessful()
        }
    }

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authenticationMessageInvalidUsername =
        stringResource(id = R.string.authentication_message_invalid_username)
    val authenticationMessageInvalidPassword =
        stringResource(id = R.string.authentication_message_invalid_password)

    BattleshipsScreen {
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

            LoginButton(enabled = state != AuthenticationState.LOADING) {
                val message = when {
                    !validateUsername(username.value) -> authenticationMessageInvalidUsername
                    !validatePassword(password.value) -> authenticationMessageInvalidPassword
                    else -> null
                }

                if (message != null) {
                    loginMessage.value = message
                    return@LoginButton
                }

                val hashedPassword = hash(password.value)

                onLogin(username.value, hashedPassword)
            }

            loginMessage.value?.let {
                AnimatedVisibility(visible = true) {
                    Text(text = it)
                }
            }

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}
