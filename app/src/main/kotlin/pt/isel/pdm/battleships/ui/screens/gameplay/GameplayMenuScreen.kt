package pt.isel.pdm.battleships.ui.screens.gameplay

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat.startActivity
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.activities.gameplay.GameConfigurationActivity
import pt.isel.pdm.battleships.activities.gameplay.LobbyActivity
import pt.isel.pdm.battleships.activities.gameplay.QuickPlayActivity
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.IconButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle
import pt.isel.pdm.battleships.ui.utils.navigateTo

/**
 * The gameplay menu screen.
 *
 * @param onBackButtonClicked the callback to be invoked when the back button is clicked.
 */
@Composable
fun GameplayMenuScreen(onBackButtonClicked: () -> Unit) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(title = stringResource(R.string.gameplay_menu_title))

        IconButton(
            onClick = {
                val intent = Intent(context, QuickPlayActivity::class.java)
                startActivity(context, intent, null)
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_play_arrow_24),
            contentDescription = stringResource(R.string.gameplay_quick_play_button_description),
            text = stringResource(id = R.string.gameplay_quick_play_button_text),
            modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
        )

        IconButton(
            onClick = {
                context.navigateTo<GameConfigurationActivity>()
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
            contentDescription = stringResource(R.string.gameplay_new_game_button_description),
            text = stringResource(id = R.string.gameplay_new_game_button_text),
            modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
        )

        IconButton(
            onClick = {
                context.navigateTo<LobbyActivity>()
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_search_24),
            contentDescription = stringResource(R.string.gameplay_search_game_button_description),
            text = stringResource(id = R.string.gameplay_search_game_button_text),
            modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
        )

        GoBackButton(onClick = onBackButtonClicked)
    }
}

private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f
