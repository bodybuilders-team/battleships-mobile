package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

const val SLIDER_RIGHT_PADDING = 32
const val GAME_CONFIG_SELECTOR_WIDTH_FACTOR = 0.5f

/**
 * Game config selector used in the game configuration menu to select a single game configuration.
 *
 * @param leftSideContent content to be shown on the left side of the selector, usually labels
 * @param rightSideContent content to be shown on the right side of the selector, usually sliders
 */
@Composable
fun GameConfigSelector(
    leftSideContent: @Composable () -> Unit,
    rightSideContent: @Composable () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(GAME_CONFIG_SELECTOR_WIDTH_FACTOR),
            contentAlignment = Alignment.Center
        ) {
            leftSideContent()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = SLIDER_RIGHT_PADDING.dp),
            contentAlignment = Alignment.Center
        ) {
            rightSideContent()
        }
    }
}
