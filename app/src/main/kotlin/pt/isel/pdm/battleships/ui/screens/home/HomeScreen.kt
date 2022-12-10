package pt.isel.pdm.battleships.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState
import pt.isel.pdm.battleships.ui.screens.shared.components.IconButton
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner

private const val LOGO_MAX_WIDTH_FACTOR = 0.6f
private const val LOGO_MAX_HEIGHT_FACTOR = 0.5f
private const val BUTTON_MAX_WIDTH_FACTOR = 0.5f

private const val WELCOME_TEXT_PADDING = 14
private const val WELCOME_TEXT_WIDTH_FACTOR = 0.9f

/**
 * Home screen.
 *
 * @param loggedIn if true, the user is logged in
 * @param username the username of the logged in user
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
    username: String?,
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
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Box {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_content_description),
                    modifier = Modifier
                        .fillMaxWidth(LOGO_MAX_WIDTH_FACTOR)
                        .fillMaxHeight(LOGO_MAX_HEIGHT_FACTOR)
                )

                if (loadingState == HomeLoadingState.LOADING)
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        LoadingSpinner()
                    }
            }

            Text(
                text = if (loggedIn)
                    stringResource(R.string.home_welcome_text, username!!)
                else
                    stringResource(R.string.home_welcome_guest_text),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(WELCOME_TEXT_WIDTH_FACTOR)
                    .padding(bottom = WELCOME_TEXT_PADDING.dp)
            )

            IconButton(
                onClick = onGameplayMenuClick,
                enabled = loadingState != HomeLoadingState.LOADING && loggedIn,
                painter = painterResource(R.drawable.ic_round_play_arrow_24),
                contentDescription = stringResource(R.string.home_playButton_description),
                text = stringResource(R.string.home_playButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            if (!loggedIn) {
                IconButton(
                    onClick = onLoginClick,
                    enabled = loadingState != HomeLoadingState.LOADING,
                    painter = painterResource(R.drawable.ic_round_login_24),
                    contentDescription = stringResource(R.string.home_loginButton_description),
                    text = stringResource(R.string.home_loginButton_text),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )

                IconButton(
                    onClick = onRegisterClick,
                    enabled = loadingState != HomeLoadingState.LOADING,
                    painter = painterResource(R.drawable.ic_round_person_add_24),
                    contentDescription = stringResource(R.string.home_registerButton_description),
                    text = stringResource(R.string.home_registerButton_text),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )
            } else {
                IconButton(
                    onClick = onLogoutClick,
                    enabled = loadingState != HomeLoadingState.LOADING,
                    painter = painterResource(R.drawable.ic_round_logout_24),
                    contentDescription = stringResource(R.string.home_logoutButton_description),
                    text = stringResource(R.string.home_logoutButton_text),
                    modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
                )
            }

            IconButton(
                onClick = onRankingClick,
                enabled = loadingState != HomeLoadingState.LOADING,
                painter = painterResource(R.drawable.ic_round_table_rows_24),
                contentDescription = stringResource(R.string.home_rankingButton_description),
                text = stringResource(R.string.home_rankingButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )

            IconButton(
                onClick = onAboutClick,
                enabled = loadingState != HomeLoadingState.LOADING,
                painter = painterResource(R.drawable.ic_round_info_24),
                contentDescription = stringResource(R.string.home_aboutButton_description),
                text = stringResource(R.string.home_aboutButton_text),
                modifier = Modifier.fillMaxWidth(BUTTON_MAX_WIDTH_FACTOR)
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        loggedIn = false,
        username = null,
        onGameplayMenuClick = {},
        onLoginClick = {},
        onRegisterClick = {},
        onLogoutClick = {},
        onRankingClick = {},
        onAboutClick = {},
        loadingState = HomeLoadingState.LOADED
    )
}

@Preview
@Composable
private fun UserHomeScreenPreview() {
    HomeScreen(
        loggedIn = true,
        username = "John Doe",
        onGameplayMenuClick = {},
        onLoginClick = {},
        onRegisterClick = {},
        onLogoutClick = {},
        onRankingClick = {},
        onAboutClick = {},
        loadingState = HomeLoadingState.LOADED
    )
}
