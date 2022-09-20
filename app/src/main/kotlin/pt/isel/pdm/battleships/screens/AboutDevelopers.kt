package pt.isel.pdm.battleships.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val ABOUT_DEVS_TITLE_PADDING = 8
private const val IMAGE_PADDING = 8
private const val DEV_INFO_PADDING = 16
private const val DEV_INFO_MAX_WIDTH_FACTOR = 0.8f
private const val DEV_INFO_CORNER_RADIUS = 8

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
fun AboutDevelopers(backToMenuCallback: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.about_devs_title),
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
        } else painterResource(id = R.drawable.github_mark_120px_plus)

        Text(text = stringResource(id = R.string.about_devs_repo_github_text))
        Image(
            painter = githubIcon,
            contentDescription = stringResource(id = R.string.github_logo_content_description),
            modifier = Modifier
                .clickable { uriHandler.openUri("https://github.com/bodybuilders-team/battleships") }
                .padding(IMAGE_PADDING.dp)
        )

        Button(
            onClick = backToMenuCallback
        ) {
            Text(text = stringResource(id = R.string.back_to_menu_button_text))
        }
    }
}

/**
 * Shows the information of a specific developer.
 * Since the email contacts are the ones from our college, ISEL, the email addresses follow a
 * specific format that only depends on the student number.
 *
 * @param number student number
 * @param name first and last name
 * @param githubLink personal github profile link
 */
@Composable
private fun DeveloperInfo(number: String, name: String, githubLink: String) {
    val uriHandler = LocalUriHandler.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(DEV_INFO_PADDING.dp)
            .fillMaxWidth(DEV_INFO_MAX_WIDTH_FACTOR)
            .clip(RoundedCornerShape(DEV_INFO_CORNER_RADIUS.dp))
            .background(Color.LightGray)
    ) {
        Text(
            text = "$number - $name",
            style = MaterialTheme.typography.h6
        )
        Row {
            val githubIcon = if (isSystemInDarkTheme()) {
                painterResource(id = R.drawable.github_mark_light_120px_plus)
            } else painterResource(id = R.drawable.github_mark_120px_plus)

            Image(
                painter = githubIcon,
                contentDescription = stringResource(id = R.string.github_logo_content_description),
                modifier = Modifier
                    .clickable { uriHandler.openUri(githubLink) }
                    .padding(IMAGE_PADDING.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.email), // TODO: Have dark mode option
                contentDescription = stringResource(id = R.string.email_icon_content_description),
                modifier = Modifier
                    .clickable { uriHandler.openUri("mailto:A$number@alunos.isel.pt") }
                    .padding(IMAGE_PADDING.dp)
            )
        }
    }
}
