package pt.isel.pdm.battleships.service.media.siren

/**
 * An Entity that can either be an [SirenEntity] or a [EmbeddedSubEntity].
 * Siren is a specification for representing hypermedia entities in JSON.
 *
 * @see <a href="https://github.com/kevinswiber/siren">Siren Specification</a>
 *
 * @property class the class of the entity (optional)
 * @property properties the properties of the entity (optional)
 * @property entities the sub-entities of the entity (optional)
 * @property actions the actions that can be performed on the entity (optional)
 * @property links the links to other entities (optional)
 */
interface Entity<T> {
    val `class`: List<String>?
    val properties: T?
    val entities: List<SubEntity>?
    val actions: List<Action>?
    val links: List<Link>?

    /**
     * Gets the link with the given [rels] from [links].
     *
     * @param rels the relations of the link
     * @return the link with the given [rels]
     */
    fun getLink(vararg rels: String) =
        links?.single { link -> rels.all { rel -> rel in link.rel } }
            ?: throw IllegalStateException("There is no links property.")

    /**
     * Gets the action with the given [name] from [actions].
     *
     * @param name the name of the action
     * @return the action with the given [name]
     */
    fun getAction(name: String) =
        actions?.single { action -> action.name == name }
            ?: throw IllegalStateException("There is no actions property.")

    /**
     * Gets the links from the actions in a map where the key is the action name and the value is
     * the action link.
     *
     * @return the links from the actions in a map
     */
    fun getActionLinks(): Map<String, String> =
        actions?.associate { it.name to it.href.path } ?: emptyMap()

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
            ?.filter { link -> rels.all { rel -> rel in link.rel } }
            ?: throw NoSuchElementException("There is no entities property.")

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
            ?: throw NoSuchElementException("There is no entities property.")
}
