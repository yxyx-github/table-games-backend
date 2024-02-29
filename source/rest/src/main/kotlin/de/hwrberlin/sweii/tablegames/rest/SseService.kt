package de.hwrberlin.sweii.tablegames.rest

import de.hwrberlin.sweii.tablegames.rest.config.RestConfiguration
import de.hwrberlin.sweii.tablegames.session.SessionService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Service
class SseService(
    private val restConfiguration: RestConfiguration,
    private val sessionService: SessionService
) {

    private val sseEmitters: MutableMap<String, MutableMap<String, SseEmitter>> = HashMap()

    fun notifyClients(sessionToken: String, data: Any) {
        logger().info("Sending $data to all clients of session: $sessionToken")
        sseEmitters[sessionToken]?.forEach {
            it.value.send(data)
            logger().info("Notified session: $sessionToken, authToken: ${it.key} about $data")
        }
    }

    fun addClient(sessionToken: String, authToken: String): SseEmitter {
        if (!sessionService.verifyUser(sessionToken, authToken)) {
            throw SseRegistrationException()
        }
        val emitter: SseEmitter = SseEmitter(restConfiguration.sseTimeout.toMillis())
        emitter.onError { removeClient(sessionToken, authToken) }
        emitter.onTimeout { removeClient(sessionToken, authToken) }

        if (!sseEmitters.containsKey(sessionToken)) {
            sseEmitters[sessionToken] = mutableMapOf(Pair(authToken, emitter))
            logger().info("Created SseEmitter for session: $sessionToken with authToken: $authToken")
            return emitter
        }

        if (sseEmitters[sessionToken]?.containsKey(authToken) == true) {
            sseEmitters[sessionToken]?.get(authToken)?.complete()
        }

        sseEmitters[sessionToken]?.set(authToken, emitter)
        logger().info("Created SseEmitter for session: $sessionToken with authToken: $authToken")
        return emitter
    }

    fun removeClient(sessionToken: String, authToken: String) {
        sseEmitters[sessionToken]?.remove(authToken)
        if (sseEmitters[sessionToken]?.isEmpty() == true) {
            sseEmitters.remove(sessionToken)
        }
        logger().info("Closed SseEmitter for session: $sessionToken with authToken: $authToken")
    }

    fun closeSession(sessionToken: String) {
        sseEmitters[sessionToken]?.forEach { it.value.complete() }
        sseEmitters.remove(sessionToken)
        logger().info("Closed SseEmitters for session: $sessionToken",)
    }
}