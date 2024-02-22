package de.hwrberlin.sweii.tablegames.rest.chess

class ChessCastleRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val kingside: Boolean,
)