package pt.isel.pdm.battleships.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R

/**
 * A back button that navigates to the previous screen.
 * The button is displayed at the bottom of the screen.
 *
 * @param navController the navigation controller
 */
@Composable
fun GoBackButton(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button_text)
            )
            Text(text = stringResource(id = R.string.back_button_text))
        }
    }
}
