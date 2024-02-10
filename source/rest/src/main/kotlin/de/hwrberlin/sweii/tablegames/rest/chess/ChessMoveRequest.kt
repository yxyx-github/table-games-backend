package de.hwrberlin.sweii.tablegames.rest.chess

data class ChessMoveRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
)