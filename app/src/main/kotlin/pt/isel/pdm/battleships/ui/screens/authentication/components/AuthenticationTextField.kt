package pt.isel.pdm.battleships.ui.screens.authentication.components

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthenticationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    maxLength: Int? = null,
    forbiddenCharacters: List<Char> = emptyList(),
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        label = {
            Text(
                text = "$label${if (required) " *" else ""}" +
                    if (errorMessage != null) " - $errorMessage" else ""
            )
        },
        value = value,
        onValueChange = {
            var newValue = it

            forbiddenCharacters.forEach { forbiddenChar ->
                newValue = newValue.replace(forbiddenChar.toString(), "")
            }

            if (maxLength != null && newValue.length > maxLength)
                newValue = newValue.substring(0 until maxLength)

            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        isError = errorMessage != null,
        visualTransformation = visualTransformation
    )
}
