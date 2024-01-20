package de.hwrberlin.sweii.tablegames.session.entity

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>