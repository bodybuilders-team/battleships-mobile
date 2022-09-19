package pt.isel.pdm.battleships.utils

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

/**
 * Composable for a hyperlink text.
 * The hyperlink is blue and underlined.
 *
 * @param text the text to show
 * @param link the actual link
 * @param style the style of the text
 */
@Composable
fun Hyperlink(text: String, link: String = text, style: TextStyle) {
    val uriHandler = LocalUriHandler.current

    ClickableText(
        text = AnnotatedString(
            text,
            SpanStyle(
                color = Color.Blue,
                fontSize = TextUnit.Unspecified,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
        ),
        onClick = {
            uriHandler.openUri(link)
        },
        style = style
    )
}
