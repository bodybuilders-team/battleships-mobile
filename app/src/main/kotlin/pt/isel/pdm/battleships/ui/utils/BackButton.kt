package pt.isel.pdm.battleships.ui.utils

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R

/**
 * A back button that navigates to the previous screen.
 *
 * @param navController the navigation controller
 */
@Composable
fun BackButton(navController: NavController) {
    Button(onClick = { navController.popBackStack() }) {
        Text(text = stringResource(id = R.string.back_button_text))
    }
}
