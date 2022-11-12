package pt.isel.pdm.battleships.ui.utils.navigation

import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a holder for the relation links map.
 *
 * @property links the relation links map
 */
@Parcelize
data class Links(val links: Map<String, String>) : Parcelable {

    /**
     * Gets the link for the specified relation.
     *
     * @param rel the relation (key) to get the link for
     * @return the link for the specified relation or null if the relation is not present
     */
    operator fun get(rel: String): String? = links[rel]

    /**
     * Returns a subset of the links that have the specified relations.
     *
     * @param rels the relations to filter the links by
     * @return a subset of the links that have the specified relations
     */
    fun subSet(vararg rels: String) = Links(links.filterKeys { rels.contains(it) })

    companion object {
        const val LINKS_KEY = "links"

        /**
         * Gets the links from the specified intent.
         *
         * @receiver the intent to get the links from
         *
         * @return the links from the specified intent
         * @throws IllegalArgumentException if the intent does not contain the links
         */
        fun Intent.getLinks(): Links =
            getParcelableExtra(LINKS_KEY)
                ?: throw IllegalArgumentException("Links not found in intent")
    }
}
