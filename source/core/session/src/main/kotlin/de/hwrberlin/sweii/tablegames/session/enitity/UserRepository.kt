package de.hwrberlin.sweii.tablegames.session.enitity

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>