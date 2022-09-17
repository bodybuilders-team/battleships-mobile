package pt.isel.pdm.battleships

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}
