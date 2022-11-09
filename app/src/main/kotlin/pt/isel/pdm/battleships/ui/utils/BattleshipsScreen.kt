package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

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
