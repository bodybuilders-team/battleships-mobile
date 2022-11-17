package pt.isel.pdm.battleships.ui.utils

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.utils.APIResult
import java.io.IOException

/**
 * Initializes a [ViewModel].
 *
 * @param T the type of the [ViewModel] to be initialized
 * @param block the block of code to be executed to initialize the [ViewModel]
 *
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
 * Tries to execute the given request and returns a [HTTPResult] with the result.
 *
 * If the request fails, the returned [HTTPResult] will be a [HTTPResult.Failure]
 * with the error message.
 *
 * @param executeRequest the request to execute
 * @return the result of the request
 */
suspend fun <T> tryExecuteHttpRequest(
    executeRequest: suspend () -> T
): HTTPResult<T> =
    try {
        HTTPResult.Success(executeRequest())
    } catch (e: IOException) {
        HTTPResult.Failure("Could not connect to the server.")
    } catch (e: UnexpectedResponseException) {
        HTTPResult.Failure("Server sent an unexpected response.")
    }

/**
 * Handles a [HTTPResult].
 *
 * @receiver the [HTTPResult] to be handled
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
 * @receiver the [APIResult] to be handled
 *
 * @param events the events that occurred in the view model
 * @param onFailure the action to be executed when the result is a failure
 *
 * @return the data of the result if it is a success, null otherwise
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
 *
 * @return the result of the request
 */
suspend fun <T> executeRequestRetrying(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>
): T {
    while (true) {
        // TODO request delay and differentiation between ApiResult.Failure
        return tryExecuteHttpRequest { request() }
            .handle(events = events)
            ?.handle(events = events)
            ?: continue
    }
}

/**
 * Tries to execute an HTTP request.
 * If the request fails, it won't be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 *
 * @return the result of the request
 */
suspend fun <T> executeRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>
): T? = tryExecuteHttpRequest { request() }
    .handle(events = events)
    ?.handle(events = events)

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
