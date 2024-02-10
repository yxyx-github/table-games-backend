package de.hwrberlin.sweii.tablegames.rest.chess

import de.hwrberlin.sweii.tablegames.chess.ChessPieceType

class ChessPromotionRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val promoteTo: ChessPieceType,
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
)