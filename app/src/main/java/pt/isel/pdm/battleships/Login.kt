package pt.isel.pdm.battleships

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun Login(backToMenuCallback: () -> Unit) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    TextField(
        value = username.value,
        onValueChange = { username.value = it },
        placeholder = { Text(text = "Username") }
    )

    TextField(
        value = password.value,
        onValueChange = { password.value = it },
        placeholder = { Text(text = "Password") }
    )

    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}
