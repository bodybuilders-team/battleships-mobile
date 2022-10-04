package pt.isel.pdm.battleships.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

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
 *
 * @param navController The navigation controller used to navigate between screens.
 */
@Composable
fun AboutScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.about_title))

        DeveloperInfoView(
            "48089",
            "André Páscoa",
            "https://github.com/devandrepascoa"
        )

        DeveloperInfoView(
            "48280",
            "André Jesus",
            "https://github.com/andre-j3sus"
        )

        DeveloperInfoView(
            "48287",
            "Nyckollas Brandão",
            "https://github.com/Nyckoka"
        )

        val uriHandler = LocalUriHandler.current

        Text(text = stringResource(id = R.string.about_repo_github_text))
        Image(
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.ic_github_light else R.drawable.ic_github_dark
            ),
            contentDescription = stringResource(id = R.string.github_logo_content_description),
            modifier = Modifier
                .clickable { uriHandler.openUri("https://github.com/bodybuilders-team/battleships") }
                .padding(IMAGE_PADDING.dp)
        )

        GoBackButton(navController)
    }
}
