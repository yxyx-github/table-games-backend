package de.hwrberlin.sweii.tablegames.rest

import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.session.entity.SessionRepository
import de.hwrberlin.sweii.tablegames.session.entity.User
import de.hwrberlin.sweii.tablegames.session.entity.UserRepository
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(val sessionRepository: SessionRepository, val userRepository: UserRepository) {

    @GetMapping("/sessions")
    fun sessions(): List<Session> {
        return sessionRepository.findAll()
    }

    @GetMapping("/users")
    fun users(): List<User> {
        return userRepository.findAll()
    }

}