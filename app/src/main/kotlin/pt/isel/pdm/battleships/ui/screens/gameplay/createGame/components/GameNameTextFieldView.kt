package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val MAX_NAME_LENGTH = 40

/**
 * Text field that allows the user to input the name of the game.
 *
 * @param gameName the current name of the game
 * @param onValueChange callback that is called when the user changes the name of the game
 */
@Composable
fun GameNameTextFieldView(
    gameName: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        label = {
            Text(text = stringResource(R.string.gameConfig_gameNameTextField_label))
        },
        value = gameName,
        onValueChange = {
            if (it.length <= MAX_NAME_LENGTH)
                onValueChange(it.trim())
        },
        placeholder = { Text(text = stringResource(R.string.gameConfig_gameNameTextField_placeholder)) },
        singleLine = true,
        modifier = Modifier
            .padding(horizontal = NAME_TEXT_FIELD_HORIZONTAL_PADDING.dp)
            .padding(bottom = NAME_TEXT_FIELD_BOTTOM_PADDING.dp)
            .fillMaxWidth()
    )
}
