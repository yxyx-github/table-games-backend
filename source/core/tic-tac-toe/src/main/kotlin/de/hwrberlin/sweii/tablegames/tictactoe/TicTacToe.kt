package de.hwrberlin.sweii.tablegames.tictactoe

import de.hwrberlin.sweii.tablegames.general.GameState

data class TicTacToe(
    val board: Array<Array<Long>> = arrayOf(
        arrayOf(-1, -1, -1),
        arrayOf(-1, -1, -1),
        arrayOf(-1, -1, -1),
    )
) : GameState {

    var lastTurn: Long = -1
        private set

    var state: State = State.RUNNING
        private set

    fun move(x: Int, y: Int, userId: Long): Boolean {
        if (lastTurn == userId || state != State.RUNNING) return false

        if (board[x][y] == -1L) {
            board[x][y] = userId
            lastTurn = userId
            state = calculateState()
            return true
        }
        return false
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

    private fun calculateState(): State {
        for (x in 0..2) {
            if (board[x][0] != -1L && board[x][1] == board[x][0] && board[x][2] == board[x][0]) return State.DECIDED
        }
        for (y in 0..2) {
            if (board[0][y] != -1L && board[1][y] == board[0][y] && board[2][y] == board[0][y]) return State.DECIDED
        }
        if (board[0][0] != -1L && board[1][1] == board[0][0] && board[2][2] == board[0][0]) return State.DECIDED
        if (board[0][2] != -1L && board[1][1] == board[0][2] && board[2][0] == board[0][2]) return State.DECIDED
        if (board.all { row -> row.all { cell -> cell != -1L } }) return State.DRAW
        return State.RUNNING
    }
}