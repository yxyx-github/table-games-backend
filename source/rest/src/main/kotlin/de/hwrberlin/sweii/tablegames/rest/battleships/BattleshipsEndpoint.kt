package de.hwrberlin.sweii.tablegames.rest.battleships

import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidActionException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidGameException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidSessionTokenException
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.battleships.Battleships
import de.hwrberlin.sweii.tablegames.rest.exceptions.NotEnoughUsersException
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
    fun state(@RequestParam sessionToken: String, @RequestParam authToken: String, @RequestParam userId: Long): BattleshipsStateResponse {
        val session: Session = sessionService.getSession(sessionToken) ?: throw InvalidSessionTokenException()
        sessionService.verifyUser(sessionToken, authToken, userId)
        val battleships: GameState = session.gameState
        if (battleships !is Battleships) {
            throw InvalidGameException("Session's game isn't Battleships")
        }
        val playerTwoId: Long = session.users.find { it.id != session.host.id }?.id ?: throw NotEnoughUsersException()
        if (battleships.playerOneId == userId) {
            return BattleshipsStateResponse(
                battleships.playerOne.board,
                battleships.playerOne.opponentBoard,
                battleships.lastTurn != userId,
                battleships.state,
                battleships.winner(playerTwoId)
            )
        }
        return BattleshipsStateResponse(
            battleships.playerTwo.board,
            battleships.playerTwo.opponentBoard,
            battleships.lastTurn != userId,
            battleships.state,
            battleships.winner(playerTwoId))
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/placeShip")
    fun placeShip(@RequestBody placeShipRequest: PlaceShipRequest) {
        if (!sessionService.verifyUser(
                placeShipRequest.sessionToken,
                placeShipRequest.authToken,
                placeShipRequest.userId
            )
        ) throw InvalidSessionTokenException()

        val battleships: GameState = sessionService.getGameState(placeShipRequest.sessionToken)
            ?: throw InvalidSessionTokenException()
        if (battleships !is Battleships) {
            throw InvalidGameException("Session's game isn't Battleships")
        }

        if (battleships.placeShip(
                placeShipRequest.x,
                placeShipRequest.y,
                placeShipRequest.shipType,
                placeShipRequest.isHorizontal,
                placeShipRequest.userId
            )
        ) {
            sessionService.updateGameState(placeShipRequest.sessionToken, battleships)
            sseService.notifyClients(placeShipRequest.sessionToken, "BATTLESHIPS ship placed")
            return
        }
        throw InvalidActionException()
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/attack")
    fun attack(@RequestBody attackRequest: AttackRequest) {
        if (!sessionService.verifyUser(
                attackRequest.sessionToken,
                attackRequest.authToken,
                attackRequest.userId
            )
        ) throw InvalidSessionTokenException()

        val battleships: GameState = sessionService.getGameState(attackRequest.sessionToken)
            ?: throw InvalidSessionTokenException()
        if (battleships !is Battleships) {
            throw InvalidGameException("Session's game isn't Battleships")
        }

        if (battleships.attack(attackRequest.x, attackRequest.y, attackRequest.userId)) {
            sessionService.updateGameState(attackRequest.sessionToken, battleships)
            sseService.notifyClients(attackRequest.sessionToken, "BATTLESHIPS attack happened")
            return
        }
        throw InvalidActionException()
    }
}
