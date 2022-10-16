package pt.isel.pdm.battleships.services.users.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents a token DTO
 *
 * @property token the token
 */
data class TokenDTOProperties(val token: String)

typealias TokenDTO = SirenEntity<TokenDTOProperties>
