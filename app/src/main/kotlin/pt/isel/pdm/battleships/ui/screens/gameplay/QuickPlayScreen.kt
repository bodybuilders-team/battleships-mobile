package pt.isel.pdm.battleships.ui.screens.gameplay

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.activities.gameplay.BoardSetupActivity
import pt.isel.pdm.battleships.viewModels.QuickPlayState
import pt.isel.pdm.battleships.viewModels.QuickPlayViewModel

private const val ANIMATION_DELAY = 1500L

@Composable
fun QuickPlayScreen(viewModel: QuickPlayViewModel) {
    LaunchedEffect(Unit) {
        delay(ANIMATION_DELAY)
        viewModel.matchmake()
    }

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (viewModel.state) {
            QuickPlayState.MATCHMAKING -> Text(text = "Matchmaking...")
            QuickPlayState.MATCHMADE -> Text(text = "Matchmade!")
            QuickPlayState.ERROR -> Text(text = "Error" + viewModel.errorMessage)
        }
        if (viewModel.state == QuickPlayState.MATCHMAKING) {
            val intent = Intent(context, BoardSetupActivity::class.java)
            startActivity(context, intent, null)
        }
    }
}
