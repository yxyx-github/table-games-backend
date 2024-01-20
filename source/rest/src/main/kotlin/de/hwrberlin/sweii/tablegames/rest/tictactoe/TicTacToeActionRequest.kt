package de.hwrberlin.sweii.tablegames.rest.tictactoe

data class TicTacToeActionRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val x: Int,
    val y: Int,
)