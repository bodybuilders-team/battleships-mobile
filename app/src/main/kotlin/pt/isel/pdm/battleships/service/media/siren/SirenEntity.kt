package pt.isel.pdm.battleships.service.media.siren

import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType

/**
 * Siren is a specification for representing hypermedia entities in JSON.
 * This class is the root of the Siren specification, being the entity sent as a response by the server.
 *
 * @see <a href="https://github.com/kevinswiber/siren">Siren Specification</a>
 *
 * @property class the class of the entity (optional)
 * @property title the title of the entity (optional)
 * @property properties the properties of the entity (optional)
 * @property entities the sub-entities of the entity (optional)
 * @property actions the actions that can be performed on the entity (optional)
 * @property links the links to other entities (optional)
 */
data class SirenEntity<T>(
    override val `class`: List<String>? = null,
    val title: String? = null,
    override val properties: T? = null,
    override val entities: List<SubEntity>? = null,
    override val actions: List<Action>? = null,
    override val links: List<Link>? = null
) : Entity<T> {

    companion object {

        /**
         * Gets the generic type of the properties of the entity.
         *
         * @param T the type of the properties of the entity
         * @return the generic type
         */
        inline fun <reified T> getType(): TypeToken<SirenEntity<T>> =
            object : TypeToken<SirenEntity<T>>() {}

        private const val APPLICATION_TYPE = "application"
        private const val SIREN_SUBTYPE = "vnd.siren+json"
        private const val SIREN_TYPE = "$APPLICATION_TYPE/$SIREN_SUBTYPE"

        val sirenMediaType = SIREN_TYPE.toMediaType()
    }
}
