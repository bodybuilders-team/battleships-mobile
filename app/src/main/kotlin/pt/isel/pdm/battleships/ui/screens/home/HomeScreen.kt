package pt.isel.pdm.battleships.ui.screens.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState
import pt.isel.pdm.battleships.ui.utils.components.IconButton

private const val LOGO_MAX_SIZE_FACTOR = 0.6f
private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

/**
 * Home screen.
 *
 * @param loggedIn if true, the user is logged in
 * @param onGameplayMenuClick callback to be invoked when the user clicks on the gameplay menu button
 * @param onLoginClick callback to be invoked when the user clicks on the login button
 * @param onRegisterClick callback to be invoked when the user clicks on the register button
 * @param onLogoutClick callback to be invoked when the user clicks on the logout button
 * @param onRankingClick callback to be invoked when the user clicks on the ranking button
 * @param onAboutClick callback to be invoked when the user clicks on the about button
 * @param loadingState the current state of the loading operation
 */
@Composable
fun HomeScreen(
    loggedIn: Boolean,
    onGameplayMenuClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRankingClick: () -> Unit,
    onAboutClick: () -> Unit,
    loadingState: HomeLoadingState
) {
    BattleshipsScreen {
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

            IconButton(
                onClick = onGameplayMenuClick,
                enabled = loadingState == HomeLoadingState.NOT_LOADING && loggedIn,
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.ic_round_play_arrow_24
                ),
                contentDescription = stringResource(R.string.main_menu_play_button_description),
                text = stringResource(id = R.string.main_menu_play_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            if (!loggedIn) {
                IconButton(
                    onClick = onLoginClick,
                    enabled = loadingState == HomeLoadingState.NOT_LOADING,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_login_24),
                    contentDescription = stringResource(
                        R.string.main_menu_login_button_description
                    ),
                    text = stringResource(id = R.string.main_menu_login_button_text),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )

                IconButton(
                    onClick = onRegisterClick,
                    enabled = loadingState == HomeLoadingState.NOT_LOADING,
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_round_person_add_24
                    ),
                    contentDescription = stringResource(
                        R.string.main_menu_register_button_description
                    ),
                    text = stringResource(
                        id = R.string.main_menu_register_button_text
                    ),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )
            } else {
                IconButton(
                    onClick = onLogoutClick,
                    enabled = loadingState == HomeLoadingState.NOT_LOADING,
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_round_logout_24
                    ),
                    contentDescription = stringResource(
                        R.string.main_menu_logout_button_description
                    ),
                    text = stringResource(id = R.string.main_menu_logout_button_text),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )
            }

            IconButton(
                onClick = onRankingClick,
                enabled = loadingState == HomeLoadingState.NOT_LOADING,
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.ic_round_table_rows_24
                ),
                contentDescription = stringResource(
                    R.string.main_menu_ranking_button_description
                ),
                text = stringResource(id = R.string.main_menu_ranking_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onAboutClick,
                enabled = loadingState == HomeLoadingState.NOT_LOADING,
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_info_24),
                contentDescription = stringResource(
                    R.string.main_menu_about_button_description
                ),
                text = stringResource(id = R.string.main_menu_about_button_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )
        }
    }
}
