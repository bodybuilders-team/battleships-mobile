package pt.isel.pdm.battleships.ui.screens.about.components

import android.net.Uri

/**
 * Information about a developer.
 *
 * @param number the student number of the developer
 * @param name the first and last name of the developer
 * @param githubLink the github profile link of the developer
 * @param email the email contact of the developer
 */
data class DeveloperInfo(
    val number: String,
    val name: String,
    val githubLink: Uri,
    val email: String
)
