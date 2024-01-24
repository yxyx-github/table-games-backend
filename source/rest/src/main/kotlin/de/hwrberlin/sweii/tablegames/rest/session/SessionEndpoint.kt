package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.general.Game
import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.User
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class SessionEndpoint(
    private val sessionService: SessionService,
    private val sseService: SseService,
) {

    @CrossOrigin(originPatterns = ["*"])
    @PostMapping("/session/create")
    fun createSession(@RequestBody sessionCreationRequest: SessionCreationRequest): SessionCreationResponse {
        val gameState: GameState = when (sessionCreationRequest.game) {
            Game.TIC_TAC_TOE -> TicTacToe()
        }
        val session = sessionService.createSession(sessionCreationRequest.host, sessionCreationRequest.game, gameState)
        return SessionCreationResponse(session.token, session.host.authToken, session.host.id!!)
    }

    @CrossOrigin(originPatterns = ["*"])
    @PostMapping("/session/join")
    fun joinSession(@RequestBody sessionJoinRequest: SessionJoinRequest): SessionJoinResponse {
        val user: User = sessionService.addUserToSession(sessionJoinRequest.sessionToken, sessionJoinRequest.name)
            ?: throw SessionJoinException()
        sseService.notifyClients(sessionJoinRequest.sessionToken, "session joined")
        return SessionJoinResponse(user.authToken, user.id!!)
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/session/close")
    fun closeSession(@RequestBody sessionCloseRequest: SessionCloseRequest) {
        if (!sessionService.closeSession(sessionCloseRequest.sessionToken, sessionCloseRequest.authToken)) {
            throw SessionCloseException()
        }
        sseService.notifyClients(sessionCloseRequest.sessionToken, "session closed")
        sseService.closeSession(sessionCloseRequest.sessionToken)
    }

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/session/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun sse(@RequestParam sessionToken: String, @RequestParam authToken: String): SseEmitter {
        return sseService.addClient(sessionToken, authToken)
    }
}