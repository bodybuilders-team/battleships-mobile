package pt.isel.pdm.battleships

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * Login/Register page.
 * This screen is responsible for the user login and register,
 * asking for the user name and password.
 *
 * Password is visually protected by [PasswordVisualTransformation].
 * TODO: this protection is fake because the keyboard listener will remember the keys and show them to the user
 *
 * @param backToMenuCallback callback to be called when the user wants to go back to the main menu
 */
@Composable
fun Login(backToMenuCallback: () -> Unit) {
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

    Column {
        LoginTextFields(username.value, password.value,
            onUsernameChangeCallback = { username.value = it },
            onPasswordChangeCallback = { password.value = it }
        )

        LoginButtons(
            onLoginClickCallback = {
                validateFields(
                    username.value, password.value,
                    loginMessageInvalidUsername,
                    loginMessageInvalidPassword
                )?.let {
                    loginMessage.value = it
                    return@LoginButtons
                }

                val hashedPassword = hashPassword(password.value)

                // Send username and hashed password to the server api

                when (MockApi.login(username.value, hashedPassword)) {
                    LoginStatus.USERNAME_NOT_FOUND -> loginMessage.value =
                        loginMessageUsernameNotFound
                    LoginStatus.WRONG_PASSWORD -> loginMessage.value = loginMessageWrongPassword
                    LoginStatus.SUCCESSFUL -> loginMessage.value = loginMessageSuccessful
                }
            },
            onRegisterClickCallback = {
                validateFields(
                    username.value, password.value,
                    loginMessageInvalidUsername,
                    loginMessageInvalidPassword
                )?.let {
                    loginMessage.value = it
                    return@LoginButtons
                }

                val hashedPassword = hashPassword(password.value)

                when (MockApi.register(username.value, hashedPassword)) {
                    RegisterStatus.USERNAME_EXISTS -> loginMessage.value = registerMessageUserExists
                    RegisterStatus.SUCCESSFUL -> loginMessage.value = registerMessageSuccessful
                }
            }
        )

        loginMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        Button(onClick = backToMenuCallback) {
            Text(text = stringResource(id = R.string.back_to_menu_button_text))
        }
    }
}

/**
 * The text fields for the login operation on the login page:
 * - Username field
 * - Password field
 *
 * @param username username to show
 * @param password password to show
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun LoginTextFields(
    username: String, password: String,
    onUsernameChangeCallback: (String) -> Unit,
    onPasswordChangeCallback: (String) -> Unit
) {
    TextField(
        value = username,
        onValueChange = onUsernameChangeCallback,
        placeholder = { Text(text = stringResource(id = R.string.login_username_placeholder_text)) }
    )

    TextField(
        value = password,
        onValueChange = onPasswordChangeCallback,
        placeholder = { Text(text = stringResource(id = R.string.login_password_placeholder_text)) },
        visualTransformation = PasswordVisualTransformation()
    )
}

/**
 * Buttons for the login operation:
 * - Login button
 * - Register button
 *
 * @param onLoginClickCallback callback to be invoked when the login button is clicked
 * @param onRegisterClickCallback callback to be invoked when the register button is clicked
 */
@Composable
fun LoginButtons(onLoginClickCallback: () -> Unit, onRegisterClickCallback: () -> Unit) {
    Row {
        Button(onClick = onLoginClickCallback) {
            Text(text = stringResource(id = R.string.login_login_button_text))
        }

        Button(onClick = onRegisterClickCallback) {
            Text(text = stringResource(id = R.string.login_register_button_text))
        }
    }
}

/**
 * Validates the username.
 *
 * @param username username
 * @return true if the username is valid, false otherwise
 */
private fun validateUsername(username: String): Boolean {
    if (username.length < 3) {
        return false
    }

    // TODO: Add more validation rules

    return true
}

/**
 * Validates the password.
 *
 * @param password password
 * @return true if the password is valid, false otherwise
 */
private fun validatePassword(password: String): Boolean {
    if (password.length < 4) {
        return false
    }

    // TODO: Add more validation rules

    return true
}

/**
 * Hashes a password.
 *
 * TODO: salt (username?)
 *
 * @param password to hash
 * @return hashed password
 */
private fun hashPassword(password: String): String {
    // Hash using SHA-256
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest(
        password.toByteArray(StandardCharsets.UTF_8)
    )

    // Convert to hexadecimal
    val sb = StringBuilder()
    for (b in encodedHash) {
        val hex = Integer.toHexString(b.toInt() and 0xff)
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }

    return sb.toString()
}


/**
 * Validates fields, returning a message in case of failure.
 *
 * @param username username to validate
 * @param password password to validate
 * @param invalidUsernameMessage message to show in case of invalid username
 * @param invalidPasswordMessage message to show in case of invalid password
 */
private fun validateFields(
    username: String, password: String,
    invalidUsernameMessage: String, invalidPasswordMessage: String
): String? {
    if (!validateUsername(username)) {
        return invalidUsernameMessage
    }
    if (!validatePassword(password)) {
        return invalidPasswordMessage
    }

    return null
}