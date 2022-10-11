package pt.isel.pdm.battleships.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.startActivity
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.activities.AboutActivity
import pt.isel.pdm.battleships.activities.RankingActivity
import pt.isel.pdm.battleships.activities.authentication.LoginActivity
import pt.isel.pdm.battleships.activities.authentication.RegisterActivity
import pt.isel.pdm.battleships.activities.gameplay.GameplayMenuActivity
import pt.isel.pdm.battleships.ui.utils.MenuButton

private const val LOGO_MAX_SIZE_FACTOR = 0.6f

/**
 * The main menu of the application.
 *
 * @param showAuthentication if true, the authentication buttons will be shown
 */
@Composable
fun HomeScreen(showAuthentication: Boolean) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_content_description),
            modifier = Modifier.fillMaxSize(LOGO_MAX_SIZE_FACTOR)
        )
        MenuButton(
            onClick = {
                val intent = Intent(context, GameplayMenuActivity::class.java)
                startActivity(context, intent, null)
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_play_arrow_24),
            iconDescription = stringResource(R.string.main_menu_play_button_content_description),
            text = stringResource(id = R.string.main_menu_play_button_text)
        )
        if (showAuthentication) {
            MenuButton(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(context, intent, null)
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_round_login_24),
                iconDescription = stringResource(
                    R.string.main_menu_login_button_content_description
                ),
                text = stringResource(id = R.string.main_menu_login_button_text)
            )
            MenuButton(
                onClick = {
                    val intent = Intent(context, RegisterActivity::class.java)
                    startActivity(context, intent, null)
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_round_person_add_24),
                iconDescription = stringResource(
                    R.string.main_menu_register_button_content_description
                ),
                text = stringResource(id = R.string.main_menu_register_button_text)
            )
        }
        MenuButton(
            onClick = {
                val intent = Intent(context, RankingActivity::class.java)
                startActivity(context, intent, null)
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_table_rows_24),
            iconDescription = stringResource(R.string.main_menu_ranking_button_content_description),
            text = stringResource(id = R.string.main_menu_ranking_button_text)
        )
        MenuButton(
            onClick = {
                val intent = Intent(context, AboutActivity::class.java)
                startActivity(context, intent, null)
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_info_24),
            iconDescription = stringResource(R.string.main_menu_about_button_content_description),
            text = stringResource(id = R.string.main_menu_about_button_text)
        )
    }
}
