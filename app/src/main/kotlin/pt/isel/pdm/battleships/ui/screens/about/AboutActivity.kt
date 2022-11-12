package pt.isel.pdm.battleships.ui.screens.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.BattleshipsApplication.Companion.TAG

/**
 * Activity for the about screen.
 */
class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AboutScreen(
                onOpenUrl = { openURL(it) },
                onSendEmail = { sendEmail(it) },
                onBackButtonClicked = { finish() }
            )
        }
    }

    /**
     * Opens the given [url].
     *
     * @param url the url to be opened
     */
    private fun openURL(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to open URL", e)
            Toast.makeText(
                /* context = */ this,
                /* text = */ "Failed to open URL",
                /* duration = */ Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Opens the email app with the given [email].
     *
     * @param email the email recipient
     */
    private fun sendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
            Toast.makeText(
                /* context = */ this,
                /* text = */ "Failed to send email",
                /* duration = */ Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val EMAIL_SUBJECT = "About Battleships App"
    }
}
