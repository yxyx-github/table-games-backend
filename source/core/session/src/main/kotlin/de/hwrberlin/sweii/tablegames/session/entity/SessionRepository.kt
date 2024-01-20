package de.hwrberlin.sweii.tablegames.session.entity

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SessionRepository : JpaRepository<Session, Long> {
    fun findByToken(token: String): Session?
    fun findByLastAccessBefore(time: LocalDateTime): List<Session>
}