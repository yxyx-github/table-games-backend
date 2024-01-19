package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.enitity.User
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class SessionEndpoint(private val sessionService: SessionService, private val sseService: SseService) {

    @PostMapping("/session/create")
    fun createSession(@RequestBody sessionCreationRequest: SessionCreationRequest): SessionCreationResponse {
        val session = sessionService.createSession(sessionCreationRequest.host)
        return SessionCreationResponse(session.token, session.host.authToken, session.host.id!!)
    }

    @PostMapping("/session/join")
    fun joinSession(@RequestBody sessionJoinRequest: SessionJoinRequest): SessionJoinResponse {
        val user: User = sessionService.addUserToSession(sessionJoinRequest.sessionToken, sessionJoinRequest.name)
            ?: throw SessionJoinException()
        sseService.notifyClients(sessionJoinRequest.sessionToken, "session joined")
        return SessionJoinResponse(user.authToken, user.id!!)
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/session/close")
    fun closeSession(@RequestBody sessionCloseRequest: SessionCloseRequest) {
        if (!sessionService.closeSession(sessionCloseRequest.sessionToken, sessionCloseRequest.authToken)) {
            throw SessionCloseException()
        }
        sseService.notifyClients(sessionCloseRequest.sessionToken, "session closed")
        sseService.closeSession(sessionCloseRequest.sessionToken)
    }

    @GetMapping("/session/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun sse(@RequestParam sessionToken: String, @RequestParam authToken: String): SseEmitter {
        return sseService.addClient(sessionToken, authToken)
    }
}