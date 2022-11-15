package pt.isel.pdm.battleships.ui.utils // ktlint-disable filename

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
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
    onFailure: (String) -> Unit = {}
): T? =
    when (this) {
        is HTTPResult.Success -> this.data
        is HTTPResult.Failure -> {
            events.emit(Event.Error(this.error))
            onFailure(this.error)
            null
        }
    }

/**
 * Handles a [APIResult].
 *
 * @param events the events that occurred in the view model
 * @param onFailure the action to be executed when the result is a failure
 */
suspend fun <T> APIResult<T>.handle(
    events: MutableSharedFlow<Event>,
    onFailure: () -> Unit = {}
): T? =
    when (this) {
        is APIResult.Success -> this.data
        is APIResult.Failure -> {
            events.emit(Event.Error(this.error.title))
            onFailure()
            null
        }
    }

/**
 * Tries to execute an HTTP request.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 */
suspend fun <T> executeRequestRetrying(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>
): T {
    while (true) {
        val httpRes = tryExecuteHttpRequest { request() }

        val res = httpRes.handle(events = events) ?: continue

        return res.handle(events = events) ?: continue
    }
}

/**
 * Tries to execute an HTTP request.
 * If the request fails, it won't be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 */
suspend fun <T> executeRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>
): T? {
    val httpRes = tryExecuteHttpRequest { request() }

    val res = httpRes.handle(events = events)

    return res?.handle(events = events)
}

/**
 * Tries to execute an HTTP request, in a coroutine.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 * @param onSuccess the action to be executed when the result is a success
 */
fun <T> ViewModel.launchAndExecuteRequestRetrying(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    onSuccess: suspend (T) -> Unit
) {
    viewModelScope.launch {
        onSuccess(executeRequestRetrying(request, events))
    }
}

/**
 * Tries to execute an HTTP request, in a coroutine.
 * If the request fails, it won't be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 * @param onFinish the action to be executed when the result is a success or a failure
 */
fun <T> ViewModel.launchAndExecuteRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    onFinish: suspend (T?) -> Unit
) {
    viewModelScope.launch {
        onFinish(executeRequest(request, events))
    }
}
