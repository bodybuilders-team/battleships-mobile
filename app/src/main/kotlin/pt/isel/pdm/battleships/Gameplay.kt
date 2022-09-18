package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pt.isel.pdm.battleships.ui.BoardView

/**
 * The gameplay screen.
 *
 * @param backToMenuCallback the callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    // TODO: Implement the gameplay screen

    Column(){
        BoardView()
        Button(onClick = backToMenuCallback) {
            Text(text = "Back to menu")
        }
    }
}
