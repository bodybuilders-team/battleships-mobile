package pt.isel.pdm.battleships.service.services.users.models

import pt.isel.pdm.battleships.service.media.siren.SirenEntity

/**
 * The Authentication Output Model.
 *
 * @property accessToken the access token
 * @property refreshToken the refresh token
 */
data class AuthenticationOutputModel(
    val accessToken: String,
    val refreshToken: String
)

/**
 * The Authentication Output.
 */
typealias AuthenticationOutput = SirenEntity<AuthenticationOutputModel>
