package pt.isel.pdm.battleships.ui.screens.authentication.register

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

/**
 * Button for login operation
 *
 * @param enabled whether the button is enabled or not
 * @param onLoginClickCallback callback to be invoked when the login button is clicked
 */
@Composable
fun RegisterButton(
    enabled: Boolean = true,
    onLoginClickCallback: () -> Unit
) {
    Row {
        Button(
            onClick = onLoginClickCallback,
            modifier = Modifier.padding(end = BUTTON_PADDING.dp),
            enabled = enabled

        ) {
            Text(text = stringResource(id = R.string.register_register_button_text))
        }
    }
}

private const val BUTTON_PADDING = 8
