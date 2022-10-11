package pt.isel.pdm.battleships.viewModels.authentication

/**
 * Represents the state of an authentication process.
 *
 * @property IDLE the initial state of the authentication process
 * @property LOADING the state of the authentication process while it is loading
 * @property SUCCESS the state of the authentication process when it is successful
 * @property ERROR the state of the authentication process when an error occurs
 */
enum class AuthenticationState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
