package pt.isel.pdm.battleships.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.AuthenticationResult
import pt.isel.pdm.battleships.service.UserService

enum class LoginState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

class LoginViewModel(
    val sessionManager: SessionManager,
    private val usersService: UserService
) : ViewModel() {
    var state by mutableStateOf(LoginState.IDLE)
    var errorMessage: String? by mutableStateOf(null)

    fun login(username: String, password: String) =
        viewModelScope.launch {
            state = LoginState.LOADING
            val res = usersService.login(username, password)
            updateState(res)
        }

    private fun updateState(res: AuthenticationResult) = when (res) {
        is AuthenticationResult.Success -> {
            sessionManager.token = res.token
            state = LoginState.SUCCESS
        }
        is AuthenticationResult.Failure -> {
            errorMessage = res.message
            state = LoginState.ERROR
        }
    }
}
