package de.hwrberlin.sweii.tablegames.rest.battleships

data class AttackRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val x: Int,
    val y: Int
)
