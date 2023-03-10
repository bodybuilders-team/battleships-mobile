package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleships.ui.screens.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMADE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMAKING
import pt.isel.pdm.battleships.ui.screens.shared.components.LoadingSpinner

/**
 * Matchmake screen.
 *
 * @param state the state of the matchmake screen
 */
@Composable
fun MatchmakeScreen(state: MatchmakeState) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when (state) {
                MATCHMADE -> Text(text = "Matchmade!")
                else -> LoadingSpinner(text = "Matchmaking...")
            }
        }
    }
}

@Preview
@Composable
private fun MatchmakeScreenPreview() {
    MatchmakeScreen(state = MATCHMADE)
}

@Preview
@Composable
private fun MatchmakeScreenMatchmakingPreview() {
    MatchmakeScreen(state = MATCHMAKING)
}
