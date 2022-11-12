package pt.isel.pdm.battleships.services.utils

import java.net.URI
import okhttp3.MediaType.Companion.toMediaType

/**
 * Represents a problem that occurred during the processing of a request.
 *
 * @property type a URI that identifies the problem type
 * @property title a short, human-readable summary of the problem
 * @property status the HTTP status code for this occurrence of the problem
 * @property detail a human-readable explanation specific to this occurrence of the problem
 * @property instance a URI that identifies the specific occurrence of the problem
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">Problem Details for HTTP APIs</a>
 */
data class Problem(
    val type: URI,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: URI? = null
) {
    companion object {
        private const val APPLICATION_TYPE = "application"
        private const val PROBLEM_SUBTYPE = "problem+json"
        private const val PROBLEM_MEDIA_TYPE = "$APPLICATION_TYPE/$PROBLEM_SUBTYPE"

        @Suppress("unused")
        val problemMediaType = PROBLEM_MEDIA_TYPE.toMediaType()
    }
}
