package pt.isel.pdm.battleships.ui.screens.authentication.register

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
import pt.isel.pdm.battleships.ui.screens.authentication.hash
import pt.isel.pdm.battleships.ui.screens.authentication.register.components.RegisterButton
import pt.isel.pdm.battleships.ui.screens.authentication.register.components.RegisterTextFields
import pt.isel.pdm.battleships.ui.screens.authentication.validateEmail
import pt.isel.pdm.battleships.ui.screens.authentication.validatePassword
import pt.isel.pdm.battleships.ui.screens.authentication.validateUsername
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

/**
 * Register screen.
 *
 * @param state Authentication state
 * @param onRegister callback to be invoked when the register button is clicked
 * @param onRegisterSuccessful callback to be invoked when the register process is successful
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RegisterScreen(
    state: AuthenticationState,
    onRegister: (String, String, String) -> Unit,
    onRegisterSuccessful: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val registerMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state) {
        if (state == AuthenticationState.SUCCESS) {
            onRegisterSuccessful()
        }
    }

    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authenticationMessageInvalidUsername =
        stringResource(id = R.string.authentication_message_invalid_username)
    val authenticationMessageInvalidEmail =
        stringResource(id = R.string.authentication_message_invalid_email)
    val authenticationMessageInvalidPassword =
        stringResource(id = R.string.authentication_message_invalid_password)

    BattleshipsScreen {
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

            RegisterButton(enabled = state != AuthenticationState.LOADING) {
                val message = when {
                    !validateEmail(email.value) -> authenticationMessageInvalidEmail
                    !validateUsername(username.value) -> authenticationMessageInvalidUsername
                    !validatePassword(password.value) -> authenticationMessageInvalidPassword
                    else -> null
                }

                if (message != null) {
                    registerMessage.value = message
                    return@RegisterButton
                }

                val hashedPassword = hash(password.value)

                onRegister(
                    email.value,
                    username.value,
                    hashedPassword
                )
            }

            registerMessage.value?.let {
                AnimatedVisibility(visible = true) {
                    Text(text = it)
                }
            }

            GoBackButton(
                onClick = onBackButtonClicked
            )
        }
    }
}
