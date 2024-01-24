package de.hwrberlin.sweii.tablegames.rest.tictactoe

import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidActionException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidGameException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidSessionTokenException
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicTacToeEndpoint(
    private val sessionService: SessionService,
    private val sseService: SseService,
) {


    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("games/tictactoe/state")
    fun state(@RequestParam sessionToken: String): TicTacToeStateResponse {
        val session: Session = sessionService.getSession(sessionToken) ?: throw InvalidSessionTokenException()
        val ticTacToe: GameState = session.gameState
        if (ticTacToe !is TicTacToe) {
            throw InvalidGameException("Sessions game isn't tic tac toe")
        }
        val userIds: List<Long> = session.users.map { user -> user.id!! }.toList()
        val turn: Int =
            if (userIds.indexOf(ticTacToe.lastTurn) == -1) 0 else (userIds.indexOf(ticTacToe.lastTurn) + 1) % userIds.size
        return TicTacToeStateResponse(
            ticTacToe.board,
            userIds[turn],
            ticTacToe.state(userIds),
            ticTacToe.winner(userIds)
        )
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/games/tictactoe/action")
    fun action(@RequestBody ticTacToeActionRequest: TicTacToeActionRequest) {
        if (!sessionService.verifyUser(ticTacToeActionRequest.sessionToken, ticTacToeActionRequest.authToken, ticTacToeActionRequest.userId)) throw InvalidSessionTokenException()

        val ticTacToe: GameState = sessionService.getGameState(ticTacToeActionRequest.sessionToken)?: throw InvalidSessionTokenException()
        if (ticTacToe !is TicTacToe) {
            throw InvalidGameException("Sessions game isn't tic tac toe")
        }

        if (ticTacToe.move(ticTacToeActionRequest.x, ticTacToeActionRequest.y, ticTacToeActionRequest.userId)) {
            sessionService.updateGameState(ticTacToeActionRequest.sessionToken, ticTacToe)
            sseService.notifyClients(ticTacToeActionRequest.sessionToken, "TIC_TAC_TOE move happened")
            return
        }
        throw InvalidActionException()
    }
}