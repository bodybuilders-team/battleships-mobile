package pt.isel.pdm.battleships.services.users.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents a token DTO.
 *
 * @property accessToken the access token
 * @property refreshToken the refresh token
 */
data class RegisterOutputDTOProperties(
    val accessToken: String,
    val refreshToken: String
)

typealias RegisterOutputDTO = SirenEntity<RegisterOutputDTOProperties>
