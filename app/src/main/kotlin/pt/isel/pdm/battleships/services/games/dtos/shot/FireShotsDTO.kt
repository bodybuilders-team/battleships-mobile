package pt.isel.pdm.battleships.services.games.dtos.shot

/**
 * A Fire Shots DTO.
 *
 * @property shots the list of shots to be fired
 */
data class FireShotsDTO(
    val shots: List<UnfiredShotDTO>
)
