package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.util.Log
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
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.FleetResponseDTOProperties
import pt.isel.pdm.battleships.services.games.dtos.shot.FireShotsDTO
import pt.isel.pdm.battleships.services.games.dtos.shot.UnfiredShotDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.PLAYING_GAME
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the [GameplayActivity].
 */
class GameplayViewModel(
    battleshipsService: BattleshipsService,
    val sessionManager: SessionManager
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val gamesService = battleshipsService.gamesService
    private val playersService = battleshipsService.playersService

    data class GameplayScreenState(
        val state: GameplayState = LOADING_GAME,
        val game: GameDTO? = null,
        val opponentBoard: OpponentBoard? = null,
        val myBoard: MyBoard? = null,
        val myTurn: Boolean? = null
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    val screenState: GameplayScreenState
        get() = _screenState

    private val _events = MutableSharedFlow<GameplayEvent>()
    val events: SharedFlow<GameplayEvent> = _events

    private var initialFleet: List<Ship>? = null
    private var gridSize: Int? = null

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        if (_screenState.state != LOADING_GAME) return

        val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

        Log.v("MyDEBUG", "About to try loading game")

        viewModelScope.launch {
            while (true) {
                val httpRes = tryExecuteHttpRequest {
                    gamesService.getGame(token, gameLink)
                }
                Log.v("MyDEBUG", "Got game http response")
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
                        _screenState = _screenState.copy(game = res.data)
                        val game = res.data

                        val getMyFleetLink =
                            game.actions?.find { it.name == Rels.GET_MY_FLEET }?.href?.path
                                ?: throw IllegalStateException("No get my fleet link found")

                        Log.v("MyDEBUG", "About to load my fleet")

                        _screenState = _screenState.copy(state = GameplayState.LOADING_MY_FLEET)
                        getMyFleet(getMyFleetLink)

                        val turn = game.properties?.state?.turn
                            ?: throw IllegalStateException("No turn found")
                        val gridSize = game.properties.config.gridSize

                        val myTurn = turn == sessionManager.username
                        _screenState = _screenState.copy(
                            game = game,
                            state = PLAYING_GAME,
                            opponentBoard = OpponentBoard(gridSize),
                            myTurn = myTurn
                        )

                        if (!myTurn)
                            waitForOpponent()

                        Log.v("MyDEBUG", "Loaded Game and My Fleet")
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

    private suspend fun getMyFleet(getMyFleetLink: String) {
        val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

        while (true) {
            val httpRes = tryExecuteHttpRequest {
                playersService.getMyFleet(
                    token = token,
                    getMyFleetLink = getMyFleetLink
                )
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

                    val gridSize = _screenState.game?.properties?.config?.gridSize
                        ?: throw IllegalStateException("No grid size found")

                    this.initialFleet = initialFleet
                    this.gridSize = gridSize

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

    fun fireShots(fireShotsLink: String, coordinates: List<Coordinate>) {
        viewModelScope.launch {
            val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")
            val coordinateDTOs = coordinates.map { it.toCoordinateDTO() }

            val httpRes = tryExecuteHttpRequest {
                playersService.fireShots(
                    token = token,
                    fireShotsLink = fireShotsLink,
                    fireShotsDTO = FireShotsDTO(coordinateDTOs.map(::UnfiredShotDTO))
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
        val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

        val gameStateLink = screenState.game?.entities
            ?.filterIsInstance<EmbeddedLink>()
            ?.find { it.rel.contains(Rels.GAME_STATE) }?.href?.path
            ?: throw IllegalStateException("Game state link not found")

        while (_screenState.myTurn == false) {
            val gameStateHttpRes = tryExecuteHttpRequest {
                gamesService.getGameState(
                    token,
                    gameStateLink
                )
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
        val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

        val getOpponentShotsLink =
            screenState.game?.actions?.find { it.name == Rels.GET_OPPONENT_SHOTS }?.href?.path
                ?: throw IllegalStateException("Get Opponent link found")

        val httpRes = tryExecuteHttpRequest {
            playersService.getOpponentShots(
                token = token,
                getOpponentShotsLink = getOpponentShotsLink
            )
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

                _screenState = _screenState.copy(
                    myBoard =
                    MyBoard(
                        gridSize
                            ?: throw IllegalStateException("Grid size not found"),
                        initialFleet ?: throw IllegalStateException("Initial fleet not found")
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
     * @property LOADING_GAME the view model is loading the game TODO
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
