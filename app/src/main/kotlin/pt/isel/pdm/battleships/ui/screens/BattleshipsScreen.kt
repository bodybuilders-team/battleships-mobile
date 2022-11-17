package pt.isel.pdm.battleships.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * A screen that displays the Battleships app.
 *
 * @param content the content to be displayed
 */
@Composable
fun BattleshipsScreen(content: @Composable () -> Unit) {
    BattleshipsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}
