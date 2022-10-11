package pt.isel.pdm.battleships.ui.screens.gameplay

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat.startActivity
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.activities.gameplay.NewGameActivity
import pt.isel.pdm.battleships.activities.gameplay.SearchGameActivity
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.MenuButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

/**
 * The gameplay menu screen.
 *
 */
@Composable
fun GameplayMenuScreen(onBackButtonClicked: () -> Unit) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.gameplay_menu_title))
        MenuButton(
            onClick = {
                val intent = Intent(context, NewGameActivity::class.java)
                startActivity(context, intent, null)
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
            iconDescription = stringResource(R.string.gameplay_new_game_button_description),
            text = stringResource(id = R.string.gameplay_new_game_button_text)
        )
        MenuButton(
            onClick = {
                val intent = Intent(context, SearchGameActivity::class.java)
                startActivity(context, intent, null)
            },
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_search_24),
            iconDescription = stringResource(R.string.gameplay_search_game_button_description),
            text = stringResource(id = R.string.gameplay_search_game_button_text)
        )
        GoBackButton(onClick = onBackButtonClicked)
    }
}
