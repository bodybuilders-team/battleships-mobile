package pt.isel.pdm.battleships.activities.utils

import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Links(val links: Map<String, String>) : Parcelable {
    operator fun get(key: String): String? = links[key]
}

const val LINKS_KEY = "links"

fun Links.subSet(vararg keys: String) = Links(links.filterKeys { keys.contains(it) })

fun Intent.getLinks(): Links {
    return getParcelableExtra(LINKS_KEY)
        ?: throw IllegalArgumentException("Links not found in intent")
}
