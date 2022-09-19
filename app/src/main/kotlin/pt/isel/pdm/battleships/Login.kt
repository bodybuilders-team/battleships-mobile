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
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            placeholder = { Text(text = "Username") }
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        val loginMessage = remember { mutableStateOf<String?>(null) }

        Row {
            Button(onClick = {
                if (!validateUsername(username.value)) {
                    loginMessage.value = "Username should have at least 3 characters."
                    return@Button
                }
                if (!validatePassword(password.value)) {
                    loginMessage.value = "Password should have at least 4 characters."
                    return@Button
                }

                val hashedPassword = hashPassword(password.value)

                // Send username and hashed password to the server api

                when (MockApi.login(username.value, hashedPassword)) {
                    "NO_USERNAME" -> loginMessage.value = "Username does not exist."
                    "WRONG_PASSWORD" -> loginMessage.value = "Password is wrong."
                    "SUCCESSFUL" -> loginMessage.value = "Login was successful."
                }
            }) {
                Text(text = "Login")
            }

            Button(onClick = {
                if (!validateUsername(username.value)) {
                    loginMessage.value = "Username should have at least 3 characters."
                }
                if (!validatePassword(password.value)) {
                    loginMessage.value = "Password should have at least 4 characters."
                }

                val hashedPassword = hashPassword(password.value)

                when (MockApi.register(username.value, hashedPassword)) {
                    "USERNAME_EXISTS" -> loginMessage.value = "Username already exists."
                    "SUCCESSFUL" -> loginMessage.value = "User registered successfully."
                }
            }) {
                Text(text = "Register instead")
            }
        }

        loginMessage.value?.let {
            AnimatedVisibility(visible = true) {
                Text(text = it)
            }
        }

        Button(onClick = backToMenuCallback) {
            Text(text = "Back to menu")
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
