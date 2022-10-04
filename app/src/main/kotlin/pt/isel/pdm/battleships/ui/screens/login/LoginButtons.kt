package pt.isel.pdm.battleships.ui.screens.login

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
 * Buttons for the login operation:
 * - Login button
 * - Register button
 *
 * @param onLoginClickCallback callback to be invoked when the login button is clicked
 * @param onRegisterClickCallback callback to be invoked when the register button is clicked
 */
@Composable
fun LoginButtons(
    onLoginClickCallback: () -> Unit,
    onRegisterClickCallback: () -> Unit
) {
    Row {
        Button(
            onClick = onLoginClickCallback,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.login_login_button_text))
        }

        Button(onClick = onRegisterClickCallback) {
            Text(text = stringResource(id = R.string.login_register_button_text))
        }
    }
}
