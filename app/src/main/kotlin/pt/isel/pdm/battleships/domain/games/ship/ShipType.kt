package pt.isel.pdm.battleships.domain.games.ship

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.battleships.services.games.dtos.ship.ShipTypeDTO

/**
 * Represents the ship class in the game.
 *
 * @property size the size of the ship
 * @property shipName the name of the ship
 * @property points the points that the ship is worth
 */
@Parcelize
enum class ShipType(
    val size: Int,
    val shipName: String,
    val points: Int
) : Parcelable {
    CARRIER(size = 5, shipName = "Carrier", points = 50),
    BATTLESHIP(size = 4, shipName = "Battleship", points = 40),
    CRUISER(size = 3, shipName = "Cruiser", points = 30),
    SUBMARINE(size = 3, shipName = "Submarine", points = 30),
    DESTROYER(size = 2, shipName = "Destroyer", points = 20);

    /**
     * Converts the ship type to a DTO.
     *
     * @return the ship type DTO
     */
    fun toShipTypeDTO() = ShipTypeDTO(shipName, size, 1, points) // TODO: Check this, quantity?

    companion object {

        /**
         * Converts a ShipTypeDTO to a ShipType.
         *
         * @param dto the DTO to convert
         * @return the ship type
         */
        fun fromShipTypeDTO(dto: ShipTypeDTO) = values().first { it.shipName == dto.shipName }
    }
}
