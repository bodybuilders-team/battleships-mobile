package pt.isel.pdm.battleships.ui.screens.gameplay.quickPlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.quickPlay.QuickPlayViewModel.QuickPlayState

/**
 * Quick play screen.
 */
@Composable
fun QuickPlayScreen(
    state: QuickPlayState,
    onMatchmade: () -> Unit,
    errorMessage: String?
) {
    LaunchedEffect(state) {
        if (state == QuickPlayState.MATCHMADE) {
            onMatchmade()
        }
    }

    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when (state) {
                QuickPlayState.MATCHMAKING, QuickPlayState.IDLE -> Text(text = "Matchmaking...")
                QuickPlayState.MATCHMADE -> Text(text = "Matchmade!")
                QuickPlayState.ERROR -> Text(text = "Error: $errorMessage")
            }
        }
    }
}
