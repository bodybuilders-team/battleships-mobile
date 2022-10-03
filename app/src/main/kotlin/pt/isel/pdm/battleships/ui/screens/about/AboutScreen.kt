package pt.isel.pdm.battleships.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.utils.BackButton

private const val ABOUT_DEVS_TITLE_PADDING = 8
const val IMAGE_PADDING = 8

/**
 * Screen shown after clicking the "About the developers" button in the main menu.
 *
 * Information shown for each developer:
 * - Student number
 * - First and last name
 * - Personal github profile link
 * - Email contact
 *
 * Also shows the github link of the app's repository.
 */
@Composable
fun AboutScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.about_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(ABOUT_DEVS_TITLE_PADDING.dp)
        )

        DeveloperInfo(
            "48089",
            "André Páscoa",
            "https://github.com/devandrepascoa"
        )

        DeveloperInfo(
            "48280",
            "André Jesus",
            "https://github.com/andre-j3sus"
        )

        DeveloperInfo(
            "48287",
            "Nyckollas Brandão",
            "https://github.com/Nyckoka"
        )

        val uriHandler = LocalUriHandler.current
        val githubIcon = if (isSystemInDarkTheme()) {
            painterResource(id = R.drawable.github_mark_light_120px_plus)
        } else {
            painterResource(id = R.drawable.github_mark_120px_plus)
        }

        Text(text = stringResource(id = R.string.about_repo_github_text))
        Image(
            painter = githubIcon,
            contentDescription = stringResource(id = R.string.github_logo_content_description),
            modifier = Modifier
                .clickable { uriHandler.openUri("https://github.com/bodybuilders-team/battleships") }
                .padding(IMAGE_PADDING.dp)
        )

        BackButton(navController)
    }
}
