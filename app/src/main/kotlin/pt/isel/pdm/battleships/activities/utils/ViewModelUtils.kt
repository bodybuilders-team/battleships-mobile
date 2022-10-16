package pt.isel.pdm.battleships.activities.utils // ktlint-disable filename

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Initializes a [ViewModel].
 *
 * @param T The type of the [ViewModel] to be initialized
 * @return The initialized [ViewModel]
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel>
ComponentActivity.viewModelInit(crossinline block: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return block() as T
            }
        }
    }
