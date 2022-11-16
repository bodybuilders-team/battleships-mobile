package pt.isel.pdm.battleships.ui.screens.about.components

import android.net.Uri

/**
 * Information about an author.
 *
 * @param number the student number of the author
 * @param name the first and last name of the author
 * @param githubLink the github profile link of the author
 * @param email the email contact of the author
 * @param imageId the id of the author's image
 */
data class AuthorInfo(
    val number: String,
    val name: String,
    val githubLink: Uri,
    val email: String,
    val imageId: Int
)
