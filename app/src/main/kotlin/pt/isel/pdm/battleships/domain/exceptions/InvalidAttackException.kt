package pt.isel.pdm.battleships.domain.exceptions

/**
 * Exception thrown when a player tries to do an invalid attack.
 *
 * @param message the message to be displayed
 */
class InvalidAttackException(message: String) : Exception(message)
