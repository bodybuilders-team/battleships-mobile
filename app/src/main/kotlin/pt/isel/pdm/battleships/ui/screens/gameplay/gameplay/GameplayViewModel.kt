package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.LinkBattleshipsService
import pt.isel.pdm.battleships.services.games.dtos.ship.FleetResponseDTOProperties
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.UnfiredShotDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.PLAYING_GAME
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the [GameplayActivity].
 */
class GameplayViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager
) : ViewModel() {
    // TODO: Implement the ViewModel

    data class GameplayScreenState(
        val state: GameplayState = LOADING_GAME,
        val gameConfig: GameConfig? = null,
        val opponentBoard: OpponentBoard? = null,
        val myBoard: MyBoard? = null,
        val myTurn: Boolean? = null
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    val screenState: GameplayScreenState
        get() = _screenState

    private val _events = MutableSharedFlow<GameplayEvent>()
    val events: SharedFlow<GameplayEvent> = _events

    private lateinit var linksBattleshipsService: LinkBattleshipsService
    private val linksGamesService by lazy { linksBattleshipsService.gamesService }
    private val linksPlayersService by lazy { linksBattleshipsService.playersService }

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        linksBattleshipsService =
            LinkBattleshipsService(
                links = mapOf(Rels.GAME to gameLink),
                sessionManager = sessionManager,
                battleshipsService = battleshipsService
            )

        if (_screenState.state != LOADING_GAME) return

        viewModelScope.launch {
            while (true) {
                val httpRes = tryExecuteHttpRequest {
                    linksGamesService.getGame()
                }

                val res = when (httpRes) {
                    is HTTPResult.Success -> {
                        httpRes.data
                    }
                    is HTTPResult.Failure -> {
                        _events.emit(GameplayEvent.Error(httpRes.error))
                        _screenState = _screenState.copy(state = LOADING_GAME)
                        continue
                    }
                }

                when (res) {
                    is APIResult.Success -> {
                        val game = res.data

                        _screenState = _screenState.copy(state = GameplayState.LOADING_MY_FLEET)

                        getMyFleet()

                        val properties = game.properties
                            ?: throw IllegalStateException("No game properties found")

                        val turn = properties.state.turn
                            ?: throw IllegalStateException("No turn found")

                        val gameConfig = GameConfig(properties.config)

                        val myTurn = turn == sessionManager.username
                        _screenState = _screenState.copy(
                            gameConfig = gameConfig,
                            state = PLAYING_GAME,
                            opponentBoard = OpponentBoard(gameConfig.gridSize),
                            myTurn = myTurn
                        )

                        if (!myTurn)
                            waitForOpponent()

                        break
                    }
                    is APIResult.Failure -> {
                        _events.emit(GameplayEvent.Error(res.error.title))
                        _screenState = _screenState.copy(state = LOADING_GAME)
                        continue
                    }
                }
            }
        }
    }

    private suspend fun getMyFleet() {
        while (true) {
            val httpRes = tryExecuteHttpRequest {
                linksPlayersService.getMyFleet()
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> {
                    httpRes.data
                }
                is HTTPResult.Failure -> {
                    _events.emit(GameplayEvent.Error(httpRes.error))
                    _screenState = _screenState.copy(state = GameplayState.LOADING_MY_FLEET)
                    continue
                }
            }

            when (res) {
                is APIResult.Success -> {
                    val getMyFleetData = res.data

                    val initialFleet = parseFleet(
                        getMyFleetData.properties ?: throw IllegalStateException("No ships found")
                    )

                    val gridSize = _screenState.gameConfig?.gridSize
                        ?: throw IllegalStateException("No game config found")

                    _screenState = _screenState.copy(
                        myBoard = MyBoard(gridSize, initialFleet)
                    )

                    break
                }
                is APIResult.Failure -> {
                    _events.emit(GameplayEvent.Error(res.error.title))
                    _screenState = _screenState.copy(state = GameplayState.LOADING_MY_FLEET)
                    continue
                }
            }
        }
    }

    fun fireShots(coordinates: List<Coordinate>) {
        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                linksPlayersService.fireShots(
                    fireShotsDTO = FireShotsDTO(
                        coordinates.map { UnfiredShotDTO(it.toCoordinateDTO()) }
                    )
                )
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> {
                    httpRes.data
                }
                is HTTPResult.Failure -> {
                    _events.emit(GameplayEvent.Error(httpRes.error))
                    _screenState = _screenState.copy(state = PLAYING_GAME)
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    val fireShotsData = res.data
                    val firedShots = fireShotsData.properties?.shots?.map {
                        it.toFiredShot()
                    }
                        ?: throw IllegalStateException("No shots found")

                    _screenState = _screenState.copy(
                        opponentBoard = _screenState.opponentBoard?.shoot(
                            firedShots.map {
                                it.coordinate to (
                                    it.result.result == "HIT" ||
                                        it.result.result == "SUNK"
                                    )
                            }
                        ),
                        myTurn = false
                    )

                    waitForOpponent()
                }
                is APIResult.Failure -> {
                    _events.emit(GameplayEvent.Error(res.error.title))
                    _screenState = _screenState.copy(state = PLAYING_GAME)
                    return@launch
                }
            }
        }
    }

    private suspend fun waitForOpponent() {
        while (_screenState.myTurn == false) {
            val gameStateHttpRes = tryExecuteHttpRequest {
                linksGamesService.getGameState()
            }

            val gameStateRes = when (gameStateHttpRes) {
                is HTTPResult.Success -> gameStateHttpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(GameplayEvent.Error(gameStateHttpRes.error))
                    _screenState = _screenState.copy(state = PLAYING_GAME)
                    continue
                }
            }

            when (gameStateRes) {
                is APIResult.Success -> {
                    val properties = gameStateRes.data.properties
                        ?: throw IllegalStateException("Game state properties are null")

                    if (properties.phase == "FINISHED") {
                        _screenState = _screenState.copy(state = GameplayState.FINISHED_GAME)
                        return
                    }

                    if (properties.turn != sessionManager.username) { // TODO: add constants
                        delay(1000L)
                    } else {
                        getOpponentShots()
                        _screenState = _screenState.copy(
                            state = PLAYING_GAME,
                            myTurn = true
                        )
                    }
                }
                is APIResult.Failure -> {
                    _events.emit(GameplayEvent.Error(gameStateRes.error.title))
                    _screenState = _screenState.copy(state = PLAYING_GAME)
                    continue
                }
            }
        }
    }

    private suspend fun getOpponentShots() {
        val httpRes = tryExecuteHttpRequest {
            linksPlayersService.getOpponentShots()
        }

        val res = when (httpRes) {
            is HTTPResult.Success -> httpRes.data
            is HTTPResult.Failure -> {
                _events.emit(GameplayEvent.Error(httpRes.error))
                _screenState = _screenState.copy(state = PLAYING_GAME)
                return
            }
        }

        when (res) {
            is APIResult.Success -> {
                val opponentShots = res.data.properties?.shots
                    ?: throw IllegalStateException("No shots found")

                val myBoard = _screenState.myBoard
                    ?: throw IllegalStateException("No my board found")

                _screenState = _screenState.copy(
                    myBoard = MyBoard(
                        size = myBoard.size,
                        initialFleet = myBoard.initialFleet
                    ).shoot(
                        opponentShots.map {
                            it.coordinate.toCoordinate()
                        }
                    )
                )
            }
            is APIResult.Failure -> {
                _events.emit(GameplayEvent.Error(res.error.title))
                _screenState = _screenState.copy(state = PLAYING_GAME)
                return
            }
        }
    }

    private fun parseFleet(fleetResponseDTOProperties: FleetResponseDTOProperties): List<Ship> =
        fleetResponseDTOProperties.ships.map {
            val shipType = ShipType.values().find { shipType ->
                shipType.shipName == it.type
            }
                ?: throw IllegalStateException("Invalid ship type")

            val orientation = Orientation.values().find { orientation ->
                orientation.name == it.orientation
            }
                ?: throw IllegalStateException("Invalid ship orientation")

            Ship(
                type = shipType,
                coordinate = it.coordinate.toCoordinate(),
                orientation = orientation,
                lives = shipType.size
            )
        }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game TODO Comment
     */
    enum class GameplayState {
        LOADING_GAME,
        LOADING_MY_FLEET,
        PLAYING_GAME,
        FINISHED_GAME
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class GameplayEvent {

        /**
         * Represents an error that occurred.
         *
         * @property message the message of the error
         */
        class Error(val message: String) : GameplayEvent()
    }
}
