package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton

private const val DIALOG_WIDTH = 0.8f

/**
 * An AlertDialog that asks the user if he wants to leave the game.
 *
 * @param onDismissRequest called when the user clicks outside the dialog or presses the back button
 * @param onLeaveGameButtonClicked called when the user clicks the "Leave game" button
 */
@Composable
fun LeaveGameAlert(
    onDismissRequest: () -> Unit,
    onLeaveGameButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.gameplay_leaveGameDialog_title)) },
        text = { Text(text = stringResource(R.string.gameplay_leaveGameDialog_text)) },
        confirmButton = {
            IconButton(
                onClick = onLeaveGameButtonClicked,
                painter = painterResource(R.drawable.ic_round_exit_to_app_24),
                contentDescription = stringResource(R.string.gameplay_leaveGameButton_description),
                text = stringResource(R.string.gameplay_leaveGameButton_text),
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {
            IconButton(
                onClick = onDismissRequest,
                painter = painterResource(R.drawable.ic_round_play_arrow_24),
                contentDescription = stringResource(R.string.gameplay_cancelLeaveGameButton_description),
                text = stringResource(R.string.gameplay_leaveGameDialog_dismissButton_text),
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxWidth(DIALOG_WIDTH)
    )
}
