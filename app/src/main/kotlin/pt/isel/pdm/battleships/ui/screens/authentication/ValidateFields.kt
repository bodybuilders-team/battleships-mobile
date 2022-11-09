package pt.isel.pdm.battleships.ui.screens.authentication

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * Validates the username.
 *
 * @param username username
 * @return true if the username is valid, false otherwise
 */
fun validateUsername(username: String): Boolean {
    if (username.length < 3) return false

    // TODO: Add more validation rules

    return true
}

fun validateEmail(email: String): Boolean {
    if (email.length < 3) return false

    // TODO: Add more validation rules

    return true
}

/**
 * Validates the password.
 *
 * @param password password
 * @return true if the password is valid, false otherwise
 */
fun validatePassword(password: String): Boolean {
    if (password.length < 4) return false

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
