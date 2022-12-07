package pt.isel.pdm.battleships.ui.screens.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R

/**
 * A back button that navigates to the previous screen.
 * The button is displayed at the bottom of the screen.
 *
 * @param onClick the action to be performed when the button is clicked
 */
@Composable
fun GoBackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        IconButton(
            onClick = onClick,
            painter = painterResource(R.drawable.ic_round_arrow_back_24),
            contentDescription = stringResource(R.string.backButton_text),
            text = stringResource(R.string.backButton_text)
        )
    }
}
