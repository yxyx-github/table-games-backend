package de.hwrberlin.sweii.tablegames.battleships

import de.hwrberlin.sweii.tablegames.general.GameState
import java.lang.IllegalStateException

data class Battleships (
    val playerOne: PlayerState = PlayerState(),
    val playerTwo: PlayerState = PlayerState(),
) : GameState {

    var state: State = State.PLACING
        private set
    var playerOneId: Long? = null
        private set
    var lastTurn: Long = -1
        private set

    fun definePlayerOne(userId: Long) {
        if (playerOneId == null) {
            lastTurn = userId
            playerOneId = userId
        }
    }

    fun winner(playerTwoId: Long): Long? {
        if (playerOne.shipsSunk.size == ShipType.entries.size) return playerOneId
        if (playerTwo.shipsSunk.size == ShipType.entries.size) return playerTwoId
        return null
    }

    fun placeShip(x: Int, y: Int, ship: ShipType, isHorizontal: Boolean, userId: Long): Boolean {
        if (playerOneId == null || this.state != State.PLACING) return false

        val state: PlayerState = if (userId == playerOneId) playerOne else playerTwo
        if (!state.shipsToPlace.contains(ship)) return false

        val board = state.board
        val endX = if (isHorizontal) x + ship.size - 1 else x
        val endY = if (!isHorizontal) y + ship.size - 1 else y

        if (!isInBounds(x, y, endX, endY) ||
            isOverlapping(board, x, y, endX, endY) ||
            isBordering(board, x, y, endX, endY)) {
            return false
        }

        for (i in 0 until ship.size) {
            if (!isHorizontal) {
                board[x][y + i] = ShipStatus.SHIP
            } else {
                board[x + i][y] = ShipStatus.SHIP
            }
        }

        state.shipsToPlace.remove(ship)
        this.state = calculateState()
        return true
    }

    fun attack(x: Int, y: Int, userId: Long): Boolean {
        if (userId == lastTurn || playerOneId == null || this.state != State.ATTACKING) return false
        if (x !in 0 until 10 || y !in 0 until 10) return false

        val state: PlayerState = if (userId == playerOneId) playerOne else playerTwo
        val opponentState: PlayerState = if (userId == playerOneId) playerTwo else playerOne

        var hit: Boolean = false

        when (opponentState.board[x][y]) {
            ShipStatus.EMPTY -> {
                opponentState.board[x][y] = ShipStatus.MISS
                state.opponentBoard[x][y] = ShipStatus.MISS
            }

            ShipStatus.SHIP -> {
                opponentState.board[x][y] = ShipStatus.HIT
                state.opponentBoard[x][y] = ShipStatus.HIT
                hit = true
                isSunkShip(x, y, state, opponentState)


            }

            else -> {
                return false
            }
        }
        if (!hit) lastTurn = userId
        this.state = calculateState()
        return true
    }

    private fun isBordering(board: Array<Array<ShipStatus>>, startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        for (x in startX..endX) {
            for (y in startY..endY) {
                if (fieldIsBordering(board, x, y)) {
                    return true
                }
            }
        }
        return false
    }

    private fun fieldIsBordering(board: Array<Array<ShipStatus>>, x: Int, y: Int): Boolean {
        if (x !in 0..<10 || y !in 0..<10) return false
        for (xOffset in -1..1) {
            for (yOffset in -1..1) {
                if (xOffset == 0 && yOffset == 0) continue
                if (x + xOffset !in 0..<10 || y + yOffset !in 0..<10) continue
                if (board[x + xOffset][y + yOffset] == ShipStatus.SHIP || board[x + xOffset][y + yOffset] == ShipStatus.HIT) return true
            }
        }
        return false
    }

    private fun isOverlapping(board: Array<Array<ShipStatus>>, startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        for (x in startX..endX) {
            for (y in startY..endY) {
                if (board[x][y] == ShipStatus.SHIP) {
                    return true
                }
            }
        }

        return false
    }

    private fun isSunkShip(x: Int, y: Int, state: PlayerState, opponentState: PlayerState): Boolean {
        if (!fieldIsBordering(opponentState.board, x, y)) {
            state.shipsSunk.add(ShipType.DESTROYER)
            markBordering(x, y, state, opponentState)
            return true
        }
        val horizontal: Boolean = (x - 1 in 0..<10 && (opponentState.board[x - 1][y] == ShipStatus.HIT || opponentState.board[x - 1][y] == ShipStatus.SHIP)) || (x + 1 in 0..<10 && (opponentState.board[x + 1][y] == ShipStatus.HIT || opponentState.board[x + 1][y] == ShipStatus.SHIP))
        var size = 1
        var endX: Int = 0
        var endY: Int = 0
        for (offset in 1..<10 - (if (horizontal) x else y) ) {
            when (opponentState.board[if (horizontal) x + offset else x][if (!horizontal) y + offset else y]) {
                ShipStatus.EMPTY, ShipStatus.MISS -> {
                    size += offset - 1
                    endX = if (horizontal) x + offset -1 else x
                    endY = if (!horizontal) y + offset -1 else y
                    break
                }

                ShipStatus.SHIP -> return false
                ShipStatus.HIT -> {
                    if ((horizontal && x + offset == 9) || (!horizontal && y + offset == 9)) {
                        size += offset
                        endX = if (horizontal) 9 else x
                        endY = if (!horizontal) 9 else y
                    }
                }
            }
        }

        var startX: Int = 0
        var startY: Int = 0
        for (offset in 1..(if (horizontal) x else y) ) {
            when (opponentState.board[if (horizontal) x - offset else x][if (!horizontal) y - offset else y]) {
                ShipStatus.EMPTY, ShipStatus.MISS -> {
                    size += offset -1
                    startX = if (horizontal) x - offset +1 else x
                    startY = if (!horizontal) y - offset +1 else y
                    break
                }

                ShipStatus.SHIP -> return false

                ShipStatus.HIT -> {
                    if ((horizontal && x - offset == 0) || (!horizontal && y - offset == 0)) {
                        size += offset
                        startX = if (horizontal) 0 else x
                        startY = if (!horizontal) 0 else y
                    }
                }
            }
        }
            state.shipsSunk.add(ShipType.entries.find { it.size == size }
                ?: throw IllegalStateException("Ship of expected size does not exist"))
            markBordering(startX, startY, endX, endY, state, opponentState)
            return true
    }

    private fun calculateState(): State {
        if (playerOne.shipsSunk.size == ShipType.entries.size || playerTwo.shipsSunk.size == ShipType.entries.size) return State.DECIDED
        if (playerOne.shipsToPlace.isEmpty() && playerTwo.shipsToPlace.isEmpty()) return State.ATTACKING
        return State.PLACING
    }

    private fun isInBounds(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        return startX in 0 ..< 10 && startY in 0 ..< 10 &&
                endX in 0 ..< 10 && endY in 0 ..< 10
    }

    private fun markBordering(startX: Int, startY: Int, endX: Int, endY: Int, state: PlayerState, opponentState: PlayerState) {
        for (x in startX..endX) {
            for (y in startY..endY) {
                markBordering(x, y, state, opponentState)
            }
        }
    }

    private fun markBordering(x: Int, y: Int, state: PlayerState, opponentState: PlayerState) {
        if (x !in 0..<10 || y !in 0..<10) return
        for (xOffset in -1..1) {
            for (yOffset in -1..1) {
                if (x + xOffset !in 0..<10 || y + yOffset !in 0..<10) continue
                if (opponentState.board[x + xOffset][y + yOffset] == ShipStatus.EMPTY) {
                    opponentState.board[x + xOffset][y + yOffset] = ShipStatus.MISS
                    state.opponentBoard[x + xOffset][y + yOffset] = ShipStatus.MISS
                }
            }
        }
    }
}
