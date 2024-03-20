package de.hwrberlin.sweii.tablegames.rest.chess

import de.hwrberlin.sweii.tablegames.chess.ChessPiece
import de.hwrberlin.sweii.tablegames.chess.State

data class ChessStateResponse(
    // [y][x] - coordinates of the piece on the board
    val board: Array<Array<ChessPiece?>>,
    val turn: Long,
    val state: State,
    val winner: Long?
)
