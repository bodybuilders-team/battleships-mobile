package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit


private enum class Page {
    MAIN_MENU,
    GAMEPLAY,
    LOGIN,
    RANKING,
    ABOUT_DEVS
}


@Composable
fun MainMenu() {
    val currentPage = remember { mutableStateOf(Page.MAIN_MENU) }

    when (currentPage.value) {
        Page.MAIN_MENU -> Column() {
            Button(onClick = { currentPage.value = Page.GAMEPLAY }) {
                Text(text = "Play", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.LOGIN }) {
                Text(text = "Login", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.RANKING }) {
                Text(text = "Ranking", color = Color.Black)
            }
            Button(onClick = { currentPage.value = Page.ABOUT_DEVS }) {
                Text(text = "About the developers", color = Color.Black)
            }
        }
        Page.GAMEPLAY -> Gameplay {
            currentPage.value = Page.MAIN_MENU
        }
        Page.LOGIN -> Login {
            currentPage.value = Page.MAIN_MENU
        }
        Page.RANKING -> Ranking {
            currentPage.value = Page.MAIN_MENU
        }
        Page.ABOUT_DEVS -> AboutDevelopers(
            backToMenuCallback = {
                currentPage.value = Page.MAIN_MENU
            })
    }
}


@Composable
fun Gameplay(backToMenuCallback: () -> Unit) {
    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}


@Composable
fun Login(backToMenuCallback: () -> Unit) {
    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}


@Composable
fun Ranking(backToMenuCallback: () -> Unit) {
    // Fetch the players from the server api


    val playerSearched = remember { mutableStateOf("") }

    // Search specific player text field
    TextField(value = playerSearched.value, onValueChange = { playerSearched.value = it })

    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}


/**
 * The "About the developers" page composable, to show after clicking the "About the developers"
 * button in the main menu.
 *
 * Shows the information on the developers who made this app and a link to
 * the github repository of the app.
 */
@Composable
fun AboutDevelopers(backToMenuCallback: () -> Unit) {
    Column() {
        Text(text = "48089 - André Páscoa")
        Text(text = "A48089@alunos.isel.pt")
        Text(text = "48280 - André Jesus")
        Text(text = "A48280@alunos.isel.pt")
        Text(text = "48287 - Nyckollas Brandão")
        Text(text = "A48287@alunos.isel.pt")
        HyperlinkText(
            fullText = "The code for the app is available at: " +
                    "https://github.com/bodybuilders-team/battleships",
            linkText = listOf("https://github.com/bodybuilders-team/battleships"),
            hyperlinks = listOf("https://github.com/bodybuilders-team/battleships")
        )
        Button(onClick = backToMenuCallback) {
            Text(text = "Back to menu")
        }
    }
}


@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}