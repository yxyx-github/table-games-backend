// BattleshipsEndpoint.kt

package de.hwrberlin.sweii.tablegames.rest.battleships

import de.hwrberlin.sweii.tablegames.battleships.BattleshipsActionRequest
import de.hwrberlin.sweii.tablegames.battleships.BattleshipsActionType
import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidActionException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidGameException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidSessionTokenException
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.battleships.BattleshipsGameState
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/games/battleships")
class BattleshipsEndpoint(
    private val sessionService: SessionService,
    private val sseService: SseService,
) {

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/state")
    fun state(@RequestParam sessionToken: String): BattleshipsStateResponse? {
        val session: Session = sessionService.getSession(sessionToken) ?: throw InvalidSessionTokenException()
        val gameState: GameState = session.gameState
        if (gameState !is BattleshipsGameState) {
            throw InvalidGameException("Session's game isn't Battleships")
        }
        val userIds: List<Long> = session.users.map { user -> user.id!! }.toList()
        val currentUser = session.users.find { user -> user.id == gameState.lastTurn }
            ?: throw InvalidActionException("Invalid state: Current user not found in session.")
        val turn: Int =
            if (userIds.indexOf(gameState.lastTurn) == -1) 0 else (userIds.indexOf(gameState.lastTurn) + 1) % userIds.size
        return currentUser.id?.let { gameState.getOpponentBoard(it) }?.let { it ->
            BattleshipsStateResponse(
                gameState.board,
                it,
                userIds[turn],
                gameState.state(userIds),
                gameState.winner(userIds),
                gameState.shipsSunk.map { it.size }
            )
        }
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/action")
    fun action(@RequestBody actionRequest: BattleshipsActionRequest) {
        if (!sessionService.verifyUser(
                actionRequest.sessionToken,
                actionRequest.authToken,
                actionRequest.userId
            )
        ) throw InvalidSessionTokenException()

        val gameState: GameState =
            sessionService.getGameState(actionRequest.sessionToken) ?: throw InvalidSessionTokenException()
        if (gameState !is BattleshipsGameState) {
            throw InvalidGameException("Session's game isn't Battleships")
        }

        when (actionRequest.action) {
            BattleshipsActionType.PLACE_SHIP -> {
                if (gameState.placeShip(
                        actionRequest.x,
                        actionRequest.y,
                        actionRequest.shipSize!!,
                        actionRequest.isHorizontal!!,
                        actionRequest.userId
                    )
                ) {
                    sessionService.updateGameState(actionRequest.sessionToken, gameState)
                    sseService.notifyClients(actionRequest.sessionToken, "Battleships ship placed")
                    return
                }
            }
            BattleshipsActionType.ATTACK -> {
                if (gameState.attack(actionRequest.x, actionRequest.y, actionRequest.userId)) {
                    sessionService.updateGameState(actionRequest.sessionToken, gameState)
                    sseService.notifyClients(actionRequest.sessionToken, "Battleships attack happened")
                    return
                }
            }
        }

        throw InvalidActionException()
    }
}
