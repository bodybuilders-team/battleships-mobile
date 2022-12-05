package pt.isel.pdm.battleships.service.media.siren

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
    override val `class`: List<String>? = null,
    val rel: List<String>,
    override val properties: T? = null,
    override val entities: List<SubEntity>? = null,
    override val actions: List<Action>? = null,
    override val links: List<Link>? = null
) : SubEntity(), Entity<T> {

    /**
     * Gets the [EmbeddedSubEntity] with the properties parsed.
     *
     * @return the [EmbeddedSubEntity] with the properties parsed
     */
    inline fun <reified T> getEmbeddedSubEntity(): EmbeddedSubEntity<T> =
        EmbeddedSubEntity(
            `class` = `class`,
            rel = rel,
            properties = jsonEncoder.fromJson(
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
