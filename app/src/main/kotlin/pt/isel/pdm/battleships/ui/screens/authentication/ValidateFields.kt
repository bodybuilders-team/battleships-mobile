package pt.isel.pdm.battleships.ui.screens.authentication

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private const val MIN_USERNAME_LENGTH = 3
const val MAX_USERNAME_LENGTH = 40

private const val MIN_PASSWORD_LENGTH = 8
const val MAX_PASSWORD_LENGTH = 127

private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"

/**
 * Validates the username.
 *
 * @param username username
 * @return true if the username is valid, false otherwise
 */
fun validateUsername(username: String): Boolean =
    username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH

/**
 * Validates the email.
 *
 * @param email the email to validate
 * @return true if the email is valid, false otherwise
 */
fun validateEmail(email: String): Boolean = email.matches(EMAIL_REGEX.toRegex())

/**
 * Validates the password.
 *
 * @param password password
 * @return true if the password is valid, false otherwise
 */
fun validatePassword(password: String): Boolean =
    password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH

/**
 * Hashes a string with sha-256.
 *
 * @param text to hash
 * @return hashed text
 */
fun hash(text: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest(
        text.toByteArray(StandardCharsets.UTF_8)
    )

    return encodedHash.fold("") { str, it -> str + "%02x".format(it) }
}
