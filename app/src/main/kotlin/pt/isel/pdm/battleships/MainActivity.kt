package pt.isel.pdm.battleships

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.battleships.ui.screens.MainMenuScreen
import pt.isel.pdm.battleships.ui.screens.about.AboutScreen
import pt.isel.pdm.battleships.ui.screens.gameplay.GameplayMenuScreen
import pt.isel.pdm.battleships.ui.screens.login.LoginScreen
import pt.isel.pdm.battleships.ui.screens.ranking.RankingScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * This activity is the main entry point of the application.
 * It is responsible for creating the main view and the view model.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = stringResource(R.string.app_name))

                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = "menu"
                        ) {
                            composable("menu") {
                                MainMenuScreen(navController)
                            }
                            composable("gameplay") {
                                GameplayMenuScreen(navController)
                            }
                            composable("login") {
                                LoginScreen(navController)
                            }
                            composable("ranking") {
                                RankingScreen(navController)
                            }
                            composable("about") {
                                AboutScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
