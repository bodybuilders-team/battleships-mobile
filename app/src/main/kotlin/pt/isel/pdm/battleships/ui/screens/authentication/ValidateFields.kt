package pt.isel.pdm.battleships.ui.screens.authentication

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

private fun validateEmail(email: String): Boolean {
    if (email.length < 3) {
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
 * Hashes a string with sha-256.
 *
 * @param text to hash
 * @return hashed text
 */
fun hash(text: String): String {
    // Hash using SHA-256
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest(
        text.toByteArray(StandardCharsets.UTF_8)
    )

    return encodedHash.fold("") { str, it -> str + "%02x".format(it) }
}

/**
 * Validates fields, returning a message in case of failure.
 *
 * @param username username to validate
 * @param password password to validate
 * @param invalidUsernameMessage message to show in case of invalid username
 * @param invalidPasswordMessage message to show in case of invalid password
 */
fun validateLoginFields(
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

fun validateEmailFields(
    email: String,
    username: String,
    password: String,
    invalidEmailMessage: String,
    invalidUsernameMessage: String,
    invalidPasswordMessage: String
): String? {
    if (!validateEmail(email)) {
        return invalidEmailMessage
    }
    if (!validateUsername(username)) {
        return invalidUsernameMessage
    }
    if (!validatePassword(password)) {
        return invalidPasswordMessage
    }
    return null
}
