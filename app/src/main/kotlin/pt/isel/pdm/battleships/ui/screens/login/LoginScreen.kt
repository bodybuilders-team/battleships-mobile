package pt.isel.pdm.battleships.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.LoginStatus
import pt.isel.pdm.battleships.MockApi
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.RegisterStatus
import pt.isel.pdm.battleships.ui.utils.BackButton

private const val LOGIN_TITLE_PADDING = 8

/**
 * Login/Register page.
 * This screen is responsible for the user login and register,
 * asking for the user name and password.
 *
 * Password is visually protected by [PasswordVisualTransformation].
 * TODO: this protection is fake because the keyboard listener will remember the keys and show them to the user
 *
 * @param navController the navigation controller
 */
@Composable
fun LoginScreen(navController: NavController) {
    val loginMessage = remember { mutableStateOf<String?>(null) }

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val loginMessageInvalidUsername = stringResource(id = R.string.login_message_invalid_username)
    val loginMessageInvalidPassword = stringResource(id = R.string.login_message_invalid_password)
    val loginMessageUsernameNotFound =
        stringResource(id = R.string.login_message_username_not_found)
    val loginMessageWrongPassword = stringResource(id = R.string.login_message_wrong_password)
    val loginMessageSuccessful = stringResource(id = R.string.login_message_successful)

    val registerMessageUserExists = stringResource(id = R.string.register_message_user_exists)
    val registerMessageSuccessful = stringResource(id = R.string.register_message_successful)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(LOGIN_TITLE_PADDING.dp)
        )

        LoginTextFields(
            username.value,
            password.value,
            onUsernameChangeCallback = { username.value = it },
            onPasswordChangeCallback = { password.value = it }
        )

        LoginButtons(
            onLoginClickCallback = {
                validateFields(
                    username.value,
                    password.value,
                    loginMessageInvalidUsername,
                    loginMessageInvalidPassword
                )?.let {
                    loginMessage.value = it
                    return@LoginButtons
                }

                val hashedPassword = hashPassword(password.value)

                // TODO Send username and hashed password to the server api

                loginMessage.value = when (MockApi.login(username.value, hashedPassword)) {
                    LoginStatus.USERNAME_NOT_FOUND -> loginMessageUsernameNotFound
                    LoginStatus.WRONG_PASSWORD -> loginMessageWrongPassword
                    LoginStatus.SUCCESSFUL -> loginMessageSuccessful
                }
            },
            onRegisterClickCallback = {
                validateFields(
                    username.value,
                    password.value,
                    loginMessageInvalidUsername,
                    loginMessageInvalidPassword
                )?.let {
                    loginMessage.value = it
                    return@LoginButtons
                }

                val hashedPassword = hashPassword(password.value)

                loginMessage.value = when (MockApi.register(username.value, hashedPassword)) {
                    RegisterStatus.USERNAME_EXISTS -> registerMessageUserExists
                    RegisterStatus.SUCCESSFUL -> registerMessageSuccessful
                }
            }
        )

        loginMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        BackButton(navController)
    }
}
