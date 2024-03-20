package de.hwrberlin.sweii.tablegames.session.entity

import jakarta.persistence.*

@Entity
data class User(
    @Column(nullable = false)
    var name: String,
    @Column(unique = true, length = 11, nullable = false)
    var authToken: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)