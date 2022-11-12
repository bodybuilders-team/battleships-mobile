package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState

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
                MatchmakeState.MATCHMAKING, MatchmakeState.IDLE -> Text(text = "Matchmaking...")
                MatchmakeState.MATCHMADE -> Text(text = "Matchmade!")
            }
        }
    }
}
