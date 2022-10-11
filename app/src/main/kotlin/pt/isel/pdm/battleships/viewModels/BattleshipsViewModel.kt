package pt.isel.pdm.battleships.viewModels

import androidx.lifecycle.ViewModel
import pt.isel.pdm.battleships.service.BattleshipsService

/**
 * Represents the ViewModel for the Battleships game.
 *
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsViewModel(
    private val battleshipsService: BattleshipsService
) : ViewModel()
