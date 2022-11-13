package pt.isel.pdm.battleships.services.games.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the properties of a Game DTO.
 *
 * @property id the id of the game
 * @property name the name of the game
 * @property creator the creator of the game
 * @property config the configuration of the game
 * @property state the state of the game
 * @property players the players of the game
 */
data class GameDTOProperties(
    val id: Int,
    val name: String,
    val creator: String,
    val config: GameConfigDTO,
    val state: GameStateDTOProperties,
    val players: List<PlayerDTO>
)

typealias GameDTO = SirenEntity<GameDTOProperties>
