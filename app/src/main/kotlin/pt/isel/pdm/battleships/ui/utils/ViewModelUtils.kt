package pt.isel.pdm.battleships.ui.utils // ktlint-disable filename

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import pt.isel.pdm.battleships.services.utils.APIResult

/**
 * Initializes a [ViewModel].
 *
 * @param T the type of the [ViewModel] to be initialized
 * @return the initialized [ViewModel]
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> ComponentActivity.viewModelInit(crossinline block: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = block() as T
        }
    }

/**
 * Handles a [HTTPResult].
 *
 * @param events the events that occurred in the view model
 * @param onFailure the action to be executed when the result is a failure
 *
 * @return the data of the result if it is a success, null otherwise
 */
suspend inline fun <T> HTTPResult<T>.handle(
    events: MutableSharedFlow<Event>,
    onFailure: () -> Unit = {}
): T? =
    when (this) {
        is HTTPResult.Success -> this.data
        is HTTPResult.Failure -> {
            events.emit(Event.Error(this.error))
            onFailure()
            null
        }
    }

/**
 * Handles a [APIResult].
 *
 * @param events the events that occurred in the view model
 * @param onSuccess the action to be executed when the result is a success
 * @param onFailure the action to be executed when the result is a failure
 */
suspend fun <T> APIResult<T>.handle(
    events: MutableSharedFlow<Event>,
    onSuccess: suspend (T) -> Unit,
    onFailure: () -> Unit = {}
) {
    when (this) {
        is APIResult.Success -> onSuccess(this.data)
        is APIResult.Failure -> {
            events.emit(Event.Error(this.error.title))
            onFailure()
        }
    }
}

/**
 * An event that occurs in a view model.
 */
interface Event {

    /**
     * An error event.
     *
     * @property message the error message
     */
    class Error(val message: String) : Event
}
