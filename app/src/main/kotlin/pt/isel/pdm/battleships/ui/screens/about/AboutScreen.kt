package pt.isel.pdm.battleships.ui.screens.about

import android.net.Uri
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.about.components.AuthorInfo
import pt.isel.pdm.battleships.ui.screens.about.components.AuthorInfoView
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

const val IMAGE_PADDING = 8
private val githubRepoUrl = Uri.parse("https://github.com/isel-leic-daw/2022-daw-leic51d-g03")

/**
 * About screen.
 *
 * Information shown for each author:
 * - Student number
 * - First and last name
 * - Personal github profile link
 * - Email contact
 *
 * Also shows the github link of the app's repository.
 *
 * @param onOpenUrl callback to be invoked when a link is clicked
 * @param onSendEmail callback to be invoked when an email is clicked
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun AboutScreen(
    onOpenUrl: (Uri) -> Unit,
    onSendEmail: (String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(R.string.about_title))

            authors.forEach { dev ->
                AuthorInfoView(
                    author = dev,
                    onSendEmail = onSendEmail,
                    onOpenUrl = onOpenUrl
                )
            }

            Text(text = stringResource(id = R.string.about_repo_github_text))
            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) {
                        R.drawable.ic_github_light
                    } else {
                        R.drawable.ic_github_dark
                    }
                ),
                contentDescription = stringResource(id = R.string.github_logo_content_description),
                modifier = Modifier
                    .clickable { onOpenUrl(githubRepoUrl) }
                    .padding(IMAGE_PADDING.dp)
            )

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}

private val authors = listOf(
    AuthorInfo(
        number = "48089",
        name = "André Páscoa",
        githubLink = Uri.parse("https://github.com/devandrepascoa"),
        email = "48089@alunos.isel.pt",
        imageId = R.drawable.andre_jesus // TODO: Change image
    ),
    AuthorInfo(
        number = "48280",
        name = "André Jesus",
        githubLink = Uri.parse("https://github.com/andre-j3sus"),
        email = "48280@alunos.isel.pt",
        imageId = R.drawable.andre_jesus
    ),
    AuthorInfo(
        number = "48287",
        name = "Nyckollas Brandão",
        githubLink = Uri.parse("https://github.com/Nyckoka"),
        email = "48287@alunos.isel.pt",
        imageId = R.drawable.andre_jesus // TODO: Change image
    )
)
