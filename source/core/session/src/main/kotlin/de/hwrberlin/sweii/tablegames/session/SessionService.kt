package de.hwrberlin.sweii.tablegames.session

import de.hwrberlin.sweii.tablegames.general.Game
import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.session.config.SessionConfiguration
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.session.entity.SessionRepository
import de.hwrberlin.sweii.tablegames.session.entity.User
import de.hwrberlin.sweii.tablegames.session.entity.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val sessionConfiguration: SessionConfiguration,
) {

    private val tokenGenerator: TokenGenerator = TokenGenerator()
    private val log: Logger = LoggerFactory.getLogger(SessionService::class.java)

    fun createSession(hostUsername: String, game: Game, initialGameState: GameState): Session {
        var token: String = tokenGenerator.generateToken()
        while (sessionRepository.findByToken(token) != null) {
            token = tokenGenerator.generateToken()
        }

        val user: User = User(hostUsername, tokenGenerator.generateToken())
        val session: Session = Session(token, user, game, initialGameState)
        userRepository.save(user)
        return sessionRepository.save(session)
    }

    fun addUserToSession(sessionToken: String, username: String): User? {
        val session: Session = sessionRepository.findByToken(sessionToken) ?: return null
        session.lastAccess = LocalDateTime.now()

        var token: String = tokenGenerator.generateToken()
        while (session.users.any { user -> user.authToken == token }) {
            token = tokenGenerator.generateToken()
        }

        val existingUser: User? = session.users.find { user -> user.name == username }
        if (existingUser != null) {
            existingUser.authToken = token
            return userRepository.save(existingUser)
        }

        if (session.users.size >= session.game.maxPlayerCount) {
            return null
        }

        val user: User = userRepository.save(User(username, token))
        session.users.add(user)
        sessionRepository.save(session)
        return user
    }

    fun getSession(sessionToken: String): Session? {
        return sessionRepository.findByToken(sessionToken)
    }

    fun getGameState(sessionToken: String): GameState? {
        return sessionRepository.findByToken(sessionToken)?.gameState
    }

    fun updateGameState(sessionToken: String, gameState: GameState): Boolean {
        val session: Session = sessionRepository.findByToken(sessionToken) ?: return false
        session.gameState = gameState
        session.lastAccess = LocalDateTime.now()
        sessionRepository.save(session)
        return true
    }


    fun closeSession(sessionToken: String, hostToken: String): Boolean {
        val session: Session = sessionRepository.findByToken(sessionToken) ?: return false
        if (session.host.authToken != hostToken) {
            return false
        }
        sessionRepository.delete(session)
        return true
    }

    fun verifyUser(sessionToken: String, authToken: String): Boolean {
        val session: Session = sessionRepository.findByToken(sessionToken) ?: return false
        return session.users.any { it.authToken == authToken }
    }

    fun verifyUser(sessionToken: String, authToken: String, userId: Long): Boolean {
        val session: Session = sessionRepository.findByToken(sessionToken) ?: return false
        return session.users.any { it.authToken == authToken && it.id == userId }
    }

    @Scheduled(
        initialDelayString = "\${table-games.session.session-cleanup-interval}",
        fixedDelayString = "\${table-games.session.session-cleanup-interval}"
    )
    fun closeExpiredSessions() {
        val expiredBefore: LocalDateTime = LocalDateTime.now().minus(sessionConfiguration.sessionDuration)
        log.info("Closing all sessions last accessed before $expiredBefore.")
        val expiredSessions: List<Session> = sessionRepository.findByLastAccessBefore(expiredBefore)

        expiredSessions.forEach {
            sessionRepository.delete(it)
        }
    }
}