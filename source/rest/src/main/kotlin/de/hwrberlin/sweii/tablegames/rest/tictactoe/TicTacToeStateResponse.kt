package de.hwrberlin.sweii.tablegames.rest.tictactoe

import de.hwrberlin.sweii.tablegames.tictactoe.State

data class TicTacToeStateResponse(
    val board: Array<Array<Long>>,
    val turn: Long,
    val state: State,
    val winner: Long?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicTacToeStateResponse

        if (!board.contentDeepEquals(other.board)) return false
        if (turn != other.turn) return false
        if (state != other.state) return false
        if (winner != other.winner) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + turn.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        return result
    }
}