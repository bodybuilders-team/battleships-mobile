package pt.isel.pdm.battleships.services.games.dtos.shot

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

data class GetOpponentShotsDTOProperties(
    val shots: List<FiredShotDTO>
)

typealias GetOpponentShotsDTO = SirenEntity<GetOpponentShotsDTOProperties>
