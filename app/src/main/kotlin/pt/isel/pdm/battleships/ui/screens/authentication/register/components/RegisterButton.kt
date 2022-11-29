package pt.isel.pdm.battleships.ui.screens.authentication.register.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.utils.components.IconButton

private const val BUTTON_PADDING = 8

/**
 * Button for register operation.
 *
 * @param enabled whether the button is enabled or not
 * @param onRegisterClickCallback callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterButton(
    enabled: Boolean = true,
    onRegisterClickCallback: () -> Unit
) {
    IconButton(
        onClick = onRegisterClickCallback,
        enabled = enabled,
        modifier = Modifier.padding(BUTTON_PADDING.dp),
        text = stringResource(R.string.register_registerButton_text),
        imageVector = ImageVector.vectorResource(R.drawable.ic_round_person_add_24),
        contentDescription = stringResource(R.string.mainMenu_registerButton_description)
    )
}
