package de.hwrberlin.sweii.tablegames.tictactoe

import de.hwrberlin.sweii.tablegames.general.GameState

data class TicTacToe(
    var lastTurn: Long = -1,
    val board: Array<Array<Long>> = arrayOf(
        arrayOf(-1, -1, -1),
        arrayOf(-1, -1, -1),
        arrayOf(-1, -1, -1),
    )
) : GameState {

    fun move(x: Int, y: Int, userId: Long): Boolean {
        if (lastTurn == userId) return false

        if (board[x][y] == -1L) {
            board[x][y] = userId
            lastTurn = userId
            return true
        }
        return false
    }

    fun state(userIds: List<Long>): State {
        if (winner(userIds) != null) return State.DECIDED
        if (board.all { row -> row.all { cell -> cell != -1L } }) return State.DRAW
        return State.RUNNING
    }

    fun winner(userIds: List<Long>): Long? {
        for (userId in userIds) {
            for (x in 0..2) {
                if (board[x][0] == userId && board[x][1] == userId && board[x][2] == userId) return userId
            }
            for (y in 0..2) {
                if (board[0][y] == userId && board[1][y] == userId && board[2][y] == userId) return userId
            }
            if (board[0][0] == userId && board[1][1] == userId && board[2][2] == userId) return userId
            if (board[0][2] == userId && board[1][1] == userId && board[2][0] == userId) return userId
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicTacToe

        return board.contentDeepEquals(other.board)
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }
}