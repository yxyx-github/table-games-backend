package de.hwrberlin.sweii.tablegames.session.enitity

import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository: JpaRepository<Session, Long> {
    fun findByToken(token: String): Session?
}