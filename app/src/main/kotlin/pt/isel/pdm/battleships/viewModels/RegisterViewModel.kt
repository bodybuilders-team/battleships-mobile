package pt.isel.pdm.battleships.viewModels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.UserService

class RegisterViewModel(
    sessionManager: SessionManager,
    private val usersService: UserService
) : AuthenticationViewModel(sessionManager) {

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.register(email, username, password)
            updateState(res)
        }
    }
}
