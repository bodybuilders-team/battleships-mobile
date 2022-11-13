package pt.isel.pdm.battleships.services.games.dtos.shot

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of the siren entity returned by the fire shots endpoint.
 */
data class FireShotsResponsePropertiesDTO(
    val shots: List<FiredShotDTO>
)

typealias FireShotsResponseDTO = SirenEntity<FireShotsResponsePropertiesDTO>
