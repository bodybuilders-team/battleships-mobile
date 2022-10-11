package pt.isel.pdm.battleships

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents the session manager responsible for holding a user's session.
 */
class SessionManager {
    var token: String? by mutableStateOf(null)
}
