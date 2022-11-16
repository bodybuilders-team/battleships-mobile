package pt.isel.pdm.battleships.services.utils.siren

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.MediaType
import java.lang.reflect.Type
import java.net.URI

/**
 * A sub-entity is an entity that is part of another entity.
 * It is represented as a link with an embedded representation.
 */
sealed class SubEntity

/**
 * A sub-entity that is an embedded link.
 *
 * @property class the class of the entity (optional)
 * @property rel the relation of the link
 * @property href the URI of the link
 * @property type the media type of the link (optional)
 * @property title the title of the link (optional)
 */
data class EmbeddedLink(
    val `class`: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()

/**
 * A sub-entity that is an embedded representation.
 * Embedded sub-entity representations retain all the characteristics of a standard entity.,
 * including a [rel] property.
 *
 * @property class the class of the entity (optional)
 * @property rel the relation of the link
 * @property properties the properties of the entity (optional)
 * @property entities the sub-entities of the entity (optional)
 * @property actions the actions that can be performed on the entity (optional)
 * @property links the links to other entities (optional)
 */
data class EmbeddedSubEntity<T>(
    val `class`: List<String>? = null,
    val rel: List<String>,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val actions: List<Action>? = null,
    val links: List<Link>? = null
) : SubEntity() {

    /*
    TODO Create Entity interface that contains these methods and is used both in EmbeddedSubEntity
     and SirenEntity
     */

    /**
     * Gets the link with the given [rels] from [links].
     *
     * @param rels the relations of the link
     *
     * @return the link with the given [rels]
     */
    fun getLink(vararg rels: String) =
        links?.single { link -> rels.all { rel -> rel in link.rel } }
            ?: throw IllegalStateException("There is no links property.")

    /**
     * Gets the action with the given [name] from [actions].
     *
     * @param name the name of the action
     *
     * @return the action with the given [name]
     */
    fun getAction(name: String) =
        actions?.single { action -> action.name == name }
            ?: throw IllegalStateException("There is no actions property.")

    /**
     * Gets the embedded sub entities with the given [rels] from [entities],
     * appropriately casting to EmbeddedSubEntity of [T].
     *
     * @param T the type of the properties of the sub entities
     * @param rels the relations of the embedded sub entities
     *
     * @return the embedded sub entities with the given [rels]
     */
    fun <T> embeddedSubEntities(vararg rels: String) =
        entities?.filterIsInstance<EmbeddedSubEntity<T>>()
            ?.filter { link ->
                rels.all { rel -> rel in link.rel }
            }
            ?: throw NoSuchElementException("There are no sub entities of that type and rels.")

    /**
     * Gets the embedded links with the given [rels] from [entities],
     * appropriately casting to EmbeddedLink.
     *
     * @param rels the relations of the embedded links
     *
     * @return the embedded links with the given [rels]
     */
    fun embeddedLinks(vararg rels: String) =
        entities?.filterIsInstance<EmbeddedLink>()
            ?.filter { link -> rels.all { rel -> rel in link.rel } }
            ?: throw NoSuchElementException("There are no embedded links of that type and rels.")

    inline fun <reified T> getEmbeddedSubEntity(): EmbeddedSubEntity<T> =
        EmbeddedSubEntity(
            `class` = `class`,
            rel = rel,
            properties = Gson().fromJson(
                jsonEncoder.toJson(properties),
                T::class.java
            ),
            entities = entities,
            actions = actions,
            links = links
        )

    companion object {
        val jsonEncoder = Gson()
    }
}

/**
 * A deserializer for the [SubEntity] class.
 */
class SubEntityDeserializer : JsonDeserializer<SubEntity> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): SubEntity =
        when (json?.asJsonObject?.has(HREF_KEY)) {
            true -> context.deserialize(json, EmbeddedLink::class.java)
            false -> context.deserialize(json, EmbeddedSubEntity::class.java)
            else -> throw IllegalArgumentException("Invalid sub-entity")
        }

    companion object {
        private const val HREF_KEY = "href"
    }
}
