package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pt.isel.pdm.battleships.utils.Hyperlink

/**
 * Screen shown after clicking the "About the developers" button in the main menu.
 *
 * Information shown for each developer:
 * - Student number
 * - First and last name
 * - Personal github profile link
 * - Email contact
 *
 * Also shows the github link of the app's repository.
 */
@Composable
fun AboutDevelopers(backToMenuCallback: () -> Unit) {
    Column {
        DeveloperInfo(
            "48089",
            "André Páscoa",
            "https://github.com/devandrepascoa"
        )

        DeveloperInfo(
            "48280",
            "André Jesus",
            "https://github.com/andre-j3sus"
        )

        DeveloperInfo(
            "48287",
            "Nyckollas Brandão",
            "https://github.com/Nyckoka"
        )

        Text(text = "The code for this app is available at: ")
        Hyperlink("https://github.com/bodybuilders-team/battleships")

        Button(onClick = backToMenuCallback) {
            Text(text = "Back to menu")
        }
    }
}

/**
 * Shows the information of a specific developer.
 * Since the email contacts are the ones from our college, ISEL, the email addresses follow a
 * specific format that only depends on the student number.
 *
 * @param number student number
 * @param name first and last name
 * @param githubLink personal github profile link
 */
@Composable
fun DeveloperInfo(number: String, name: String, githubLink: String) {
    Text(text = "$number - $name")
    Hyperlink("Github", githubLink)
    Hyperlink("Email\n", "mailto:A$number@alunos.isel.pt")
}
