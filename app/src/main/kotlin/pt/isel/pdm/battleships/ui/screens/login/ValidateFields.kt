package pt.isel.pdm.battleships.ui.screens.login

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

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
fun hashPassword(password: String): String {
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
fun validateFields(
    username: String,
    password: String,
    invalidUsernameMessage: String,
    invalidPasswordMessage: String
): String? {
    if (!validateUsername(username)) {
        return invalidUsernameMessage
    }
    if (!validatePassword(password)) {
        return invalidPasswordMessage
    }

    return null
}
