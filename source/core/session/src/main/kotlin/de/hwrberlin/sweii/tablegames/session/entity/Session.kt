package de.hwrberlin.sweii.tablegames.session.entity

import de.hwrberlin.sweii.tablegames.general.Game
import de.hwrberlin.sweii.tablegames.general.GameState
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Session(
    @Id
    @Column(unique = true, length = 11, nullable = false)
    var token: String,
    @OneToOne(orphanRemoval = true)
    var host: User,
    @Column(nullable = false)
    var game: Game,
    @Column(nullable = false)
    @Lob
    var gameState: GameState,
    @OneToMany(orphanRemoval = true)
    var users: MutableList<User> = mutableListOf(host),
    var lastAccess: LocalDateTime = LocalDateTime.now(),
)