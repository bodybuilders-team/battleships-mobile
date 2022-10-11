package pt.isel.pdm.battleships.viewModels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.UserService

class LoginViewModel(
    sessionManager: SessionManager,
    private val usersService: UserService
) : AuthenticationViewModel(sessionManager) {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.login(username, password)
            updateState(res)
        }
    }
}
