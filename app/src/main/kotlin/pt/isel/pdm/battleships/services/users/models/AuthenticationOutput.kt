package pt.isel.pdm.battleships.services.users.models

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of an [AuthenticationOutput]. // TODO: Fix comments!
 *
 * @property accessToken the access token
 * @property refreshToken the refresh token
 */
data class AuthenticationOutputModel(
    val accessToken: String,
    val refreshToken: String
)

/**
 * An Authentication Output.
 */
typealias AuthenticationOutput = SirenEntity<AuthenticationOutputModel>
