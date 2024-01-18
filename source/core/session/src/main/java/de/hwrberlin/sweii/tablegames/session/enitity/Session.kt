package de.hwrberlin.sweii.tablegames.session.enitity

import jakarta.persistence.*

@Entity
data class Session(
    @Column(unique = true, length = 11, nullable = false)
    var token: String,
    @OneToOne(orphanRemoval = true)
    var host: User,
    @OneToMany(orphanRemoval = true)
    var users: MutableList<User> = mutableListOf(host),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)