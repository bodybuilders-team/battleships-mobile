package pt.isel.pdm.battleships.ui.screens.about

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val DEV_INFO_PADDING = 16
private const val DEV_INFO_MAX_WIDTH_FACTOR = 0.8f
private const val DEV_INFO_CORNER_RADIUS = 8

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
fun DeveloperInfoView(number: String, name: String, githubLink: String) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

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
            Image(
                painter = painterResource(id = R.drawable.ic_github_dark),
                contentDescription = stringResource(id = R.string.github_logo_content_description),
                modifier = Modifier
                    .clickable { uriHandler.openUri(githubLink) }
                    .padding(IMAGE_PADDING.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.email),
                contentDescription = stringResource(id = R.string.email_icon_content_description),
                modifier = Modifier
                    .clickable {
                        try {
                            uriHandler.openUri("mailto:A$number@alunos.isel.pt")
                        } catch (e: ActivityNotFoundException) {
                            Toast
                                .makeText(context, "Mail client not found", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    .padding(IMAGE_PADDING.dp)
            )
        }
    }
}
