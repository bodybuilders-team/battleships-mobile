package pt.isel.pdm.battleships.domain.games

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.battleships.domain.games.ship.Ship

/**
 * A cell in the board.
 *
 * @property coordinate the coordinate of the cell
 * @property wasHit if the cell was hit
 */
sealed class Cell(
    open val coordinate: Coordinate,
    open val wasHit: Boolean
) : Parcelable

/**
 * An empty cell.
 */
@Parcelize
data class WaterCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean
) :
    Cell(coordinate, wasHit), Parcelable

/**
 * A cell that contains a ship.
 *
 * @property ship the ship that is in this cell
 */
@Parcelize
data class ShipCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean,
    val ship: Ship
) :
    Cell(coordinate, wasHit), Parcelable
