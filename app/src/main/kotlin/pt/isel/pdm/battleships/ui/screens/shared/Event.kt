package pt.isel.pdm.battleships.ui.screens.shared

import androidx.lifecycle.ViewModel

/**
 * An event that occurs in a [ViewModel].
 */
interface Event {

    /**
     * An error event.
     *
     * @property message the error message
     */
    class Error(val message: String) : Event
}
