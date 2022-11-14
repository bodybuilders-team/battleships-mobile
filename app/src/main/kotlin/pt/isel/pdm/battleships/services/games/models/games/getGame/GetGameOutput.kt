package pt.isel.pdm.battleships.services.games.models.games.getGame

import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.games.models.games.GameStateModel
import pt.isel.pdm.battleships.services.games.models.games.PlayerModel
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The Get Game Output Model.
 *
 * @property id the id of the game
 * @property name the name of the game
 * @property creator the creator of the game
 * @property config the configuration of the game
 * @property state the state of the game
 * @property players the players of the game
 */
data class GetGameOutputModel(
    val id: Int,
    val name: String,
    val creator: String,
    val config: GameConfigModel,
    val state: GameStateModel,
    val players: List<PlayerModel>
)

/**
 * The Get Game Output.
 */
typealias GetGameOutput = SirenEntity<GetGameOutputModel>
