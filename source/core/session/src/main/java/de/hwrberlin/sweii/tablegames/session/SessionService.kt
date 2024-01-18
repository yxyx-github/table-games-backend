package de.hwrberlin.sweii.tablegames.session

import de.hwrberlin.sweii.tablegames.session.enitity.Session
import de.hwrberlin.sweii.tablegames.session.enitity.SessionRepository
import de.hwrberlin.sweii.tablegames.session.enitity.User
import de.hwrberlin.sweii.tablegames.session.enitity.UserRepository
import org.springframework.stereotype.Component

@Component
class SessionService(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) {

    private val tokenGenerator: TokenGenerator = TokenGenerator()

    fun createSession(hostUsername: String): Session {
        var token: String = tokenGenerator.generateToken()
        while (sessionRepository.findByToken(token) != null) {
            token = tokenGenerator.generateToken()
        }

        val user: User = User(hostUsername, tokenGenerator.generateToken())
        val session: Session = Session(token, user)
        userRepository.save(user)
        return sessionRepository.save(session);
    }

    fun addUserToSession(sessionToken: String, username: String): User? {
        val session: Session = sessionRepository.findByToken(sessionToken)?: return null

        var token: String = tokenGenerator.generateToken()
        while (session.users.any { user -> user.authToken == token }) {
            token = tokenGenerator.generateToken()
        }

        val existingUser: User? = session.users.find { user -> user.name == username }
        if (existingUser != null) {
            existingUser.authToken = token
            return userRepository.save(existingUser)
        }

        val user: User = userRepository.save(User(username, token))
        session.users.add(user)
        sessionRepository.save(session)
        return user
    }

    fun closeSession(sessionToken: String, hostToken: String): Boolean {
        val session: Session = sessionRepository.findByToken(sessionToken)?: return false
        if (session.host.authToken != hostToken) {
            return false
        }
        sessionRepository.delete(session)
        return true
    }

}