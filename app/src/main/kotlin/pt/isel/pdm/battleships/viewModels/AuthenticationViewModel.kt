package pt.isel.pdm.battleships.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.AuthenticationResult

enum class AuthenticationState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {
    var state by mutableStateOf(AuthenticationState.IDLE)
    var errorMessage: String? by mutableStateOf(null)

    protected fun updateState(res: AuthenticationResult) = when (res) {
        is AuthenticationResult.Success -> {
            sessionManager.token = res.token
            state = AuthenticationState.SUCCESS
        }
        is AuthenticationResult.Failure -> {
            errorMessage = res.message
            state = AuthenticationState.ERROR
        }
    }
}
