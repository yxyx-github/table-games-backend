package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.battleships.Battleships
import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.general.Game
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidSessionTokenException
import de.hwrberlin.sweii.tablegames.rest.general.GameResponse
import de.hwrberlin.sweii.tablegames.rest.logger
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.session.entity.User
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/session")
class SessionEndpoint(
    private val sessionService: SessionService,
    private val sseService: SseService,
) {

    @CrossOrigin(originPatterns = ["*"])
    @PostMapping("/create")
    fun createSession(@RequestBody sessionCreationRequest: SessionCreationRequest): SessionCreationResponse {
        val session: Session = when (sessionCreationRequest.game) {
            Game.TIC_TAC_TOE -> sessionService.createSession(sessionCreationRequest.host, sessionCreationRequest.game, TicTacToe())
            Game.CHESS -> {
                val chess: Chess = Chess()
                val sess: Session = sessionService.createSession(sessionCreationRequest.host, sessionCreationRequest.game, chess)
                chess.defineWhiteUser(sess.host.id!!)
                sessionService.updateGameState(sess.token, chess)
                sess
            }

            Game.BATTLESHIPS -> {
                val battleships: Battleships = Battleships()
                val sess: Session = sessionService.createSession(sessionCreationRequest.host, sessionCreationRequest.game, battleships)
                battleships.definePlayerOne(sess.host.id!!)
                sessionService.updateGameState(sess.token, battleships)
                sess
            }
        }
        logger().info("Created a Session with token: ${session.token} and game: ${sessionCreationRequest.game}")
        return SessionCreationResponse(session.token, session.host.authToken, session.host.id!!)
    }

    @CrossOrigin(originPatterns = ["*"])
    @PostMapping("/join")
    fun joinSession(@RequestBody sessionJoinRequest: SessionJoinRequest): SessionJoinResponse {
        val user: User = sessionService.addUserToSession(sessionJoinRequest.sessionToken, sessionJoinRequest.name) ?: throw SessionJoinException()
        val session: Session = sessionService.getSession(sessionJoinRequest.sessionToken) ?: throw InvalidSessionTokenException()
        logger().info("${sessionJoinRequest.name} with authToken ${user.authToken} joined session: ${sessionJoinRequest.sessionToken}")
        sseService.notifyClients(sessionJoinRequest.sessionToken, "session joined")
        return SessionJoinResponse(
            user.authToken,
            UserResponse(user.id!!, user.name, user.id == session.host.id),
            GameResponse(session.game.name, session.game.maxPlayerCount, session.game.minPlayerCount)
        )
    }

    @CrossOrigin(originPatterns = ["*"])
    @PostMapping("/info")
    fun sessionInfo(@RequestBody sessionInfoRequest: SessionInfoRequest): SessionInfoResponse {
        if (!sessionService.verifyUser(
                sessionInfoRequest.sessionToken,
                sessionInfoRequest.authToken,
                sessionInfoRequest.userId
            )
        ) {
            throw InvalidSessionTokenException();
        }
        val session: Session = sessionService.getSession(sessionInfoRequest.sessionToken) ?: throw InvalidSessionTokenException()
        val user: User = session.users.find { user: User -> user.id == sessionInfoRequest.userId && user.authToken == sessionInfoRequest.authToken }!!
        logger().info("Send info about session: ${sessionInfoRequest.sessionToken}")
        return SessionInfoResponse(
            GameResponse(
                session.game.name,
                session.game.maxPlayerCount,
                session.game.maxPlayerCount
            ), UserResponse(user.id!!, user.name, user.id == session.host.id)
        )
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/close")
    fun closeSession(@RequestBody sessionCloseRequest: SessionCloseRequest) {
        if (!sessionService.closeSession(sessionCloseRequest.sessionToken, sessionCloseRequest.authToken)) {
            throw SessionCloseException()
        }
        sseService.notifyClients(sessionCloseRequest.sessionToken, "session closed")
        sseService.closeSession(sessionCloseRequest.sessionToken)
        logger().info("Closed session: ${sessionCloseRequest.sessionToken}")
    }

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun sse(@RequestParam sessionToken: String, @RequestParam authToken: String): SseEmitter {
        return sseService.addClient(sessionToken, authToken)
    }
}