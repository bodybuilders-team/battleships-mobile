package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

/**
 * The button that allows the player to leave the game.
 *
 * @param onClick the function to be called when the button is clicked
 */
@Composable
fun LeaveGameButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        IconButton(
            onClick = onClick,
            painter = painterResource(R.drawable.ic_round_exit_to_app_24),
            contentDescription = stringResource(R.string.gameplay_leaveGameButton_description),
            text = stringResource(R.string.gameplay_leaveGameButton_text)
        )
    }
}
