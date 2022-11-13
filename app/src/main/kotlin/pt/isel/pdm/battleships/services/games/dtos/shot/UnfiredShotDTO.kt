package pt.isel.pdm.battleships.services.games.dtos.shot

import pt.isel.pdm.battleships.services.games.dtos.CoordinateDTO

/**
 * An Unfired Shot DTO.
 *
 * @property coordinate the coordinate of the shot
 */
data class UnfiredShotDTO(
    val coordinate: CoordinateDTO
)
