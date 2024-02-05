package de.hwrberlin.sweii.tablegames.battleships

import de.hwrberlin.sweii.tablegames.general.GameState

data class BattleshipsGameState(
    var lastTurn: Long = -1,
    val board: Array<Array<ShipStatus>> = Array(10) { Array(10) { ShipStatus.EMPTY } },
    val opponentBoard: Array<Array<ShipStatus>> = Array(10) { Array(10) { ShipStatus.EMPTY } },
    val shipsSunk: MutableList<ShipType> = mutableListOf()
) : GameState {

    enum class ShipStatus { EMPTY, SHIP, HIT, MISS }
    enum class ShipType(val size: Int) { DESTROYER(2), SUBMARINE(3), CRUISER(3), BATTLESHIP(4), CARRIER(5) }

    fun placeShip(x: Int, y: Int, size: Int, isHorizontal: Boolean, userId: Long): Boolean {
        if (lastTurn != userId || size <= 0) {
            return false
        }

        val endX = if (isHorizontal) x else x + size - 1
        val endY = if (isHorizontal) y + size - 1 else y

        if (!isValidPlacement(x, y, endX, endY) || !isStraightLine(x, y, endX, endY)) {
            return false
        }

        // Place the ship on the board
        for (i in 0 until size) {
            if (isHorizontal) {
                board[x][y + i] = ShipStatus.SHIP
            } else {
                board[x + i][y] = ShipStatus.SHIP
            }
        }

        lastTurn = userId
        return true
    }

    fun attack(x: Int, y: Int, userId: Long): Boolean {
        if (lastTurn != userId) {
            return false
        }

        // Ensure attack coordinates are within the board boundaries
        if (x !in 0 until 10 || y !in 0 until 10) {
            return false
        }

        when (board[x][y]) {
            ShipStatus.EMPTY -> {
                board[x][y] = ShipStatus.MISS
                opponentBoard[x][y] = ShipStatus.MISS
            }
            ShipStatus.SHIP -> {
                board[x][y] = ShipStatus.HIT
                opponentBoard[x][y] = ShipStatus.HIT

                // Check if the ship is sunk
                checkSunkShip(x, y)
            }
            else -> {
                return false // Invalid attack, already attacked this position
            }
        }

        lastTurn = userId
        return true
    }

    private fun checkSunkShip(x: Int, y: Int) {
        val shipType = findShipType(x, y)
        if (shipType != null && !shipsSunk.contains(shipType) && isShipSunk(shipType)) {
            shipsSunk.add(shipType)
        }
    }

    private fun findShipType(x: Int, y: Int): ShipType? {
        for (shipType in ShipType.entries) {
            val size = shipType.size
            if (isHorizontalShip(x, y, size) || isVerticalShip(x, y, size)) {
                return shipType
            }
        }
        return null
    }

    private fun isShipSunk(shipType: ShipType): Boolean {
        val size = shipType.size
        return board.flatten().count { it == ShipStatus.HIT } == size
    }

    private fun isHorizontalShip(x: Int, y: Int, size: Int): Boolean {
        return y + size <= 10 && (y until y + size).all { board[x][it] == ShipStatus.HIT }
    }

    private fun isVerticalShip(x: Int, y: Int, size: Int): Boolean {
        return x + size <= 10 && (x until x + size).all { board[it][y] == ShipStatus.HIT }
    }

    fun state(userIds: List<Long>): State {
        val winner = winner(userIds)
        return when {
            winner != null -> State.DECIDED
            board.all { row -> row.all { cell -> cell == ShipStatus.MISS } } -> State.DRAW
            else -> State.RUNNING
        }
    }

    fun winner(userIds: List<Long>): Long? {
        for (userId in userIds) {
            if (board.flatten().all { cell -> cell == ShipStatus.MISS || cell == ShipStatus.EMPTY }) {
                // All ships of the opponent have been sunk
                return userId
            }
        }
        return null
    }

    fun getOpponentBoard(userId: Long): Array<Array<ShipStatus>> {
        // Return a modified version of the opponent's board (only hits and misses are visible)
        return if (lastTurn != userId) opponentBoard else Array(10) { Array(10) { ShipStatus.EMPTY } }
    }

    fun resetGame() {
        // Reset the game state for a new round or session
        lastTurn = -1
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                board[i][j] = ShipStatus.EMPTY
                opponentBoard[i][j] = ShipStatus.EMPTY
            }
        }
        shipsSunk.clear()
    }

    private fun isValidPlacement(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        // Check if the placement is within the board boundaries
        return startX in 0 until 10 && startY in 0 until 10 &&
                endX in 0 until 10 && endY in 0 until 10
    }

    private fun isStraightLine(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        // Check if the ship is placed in a straight line (horizontal or vertical)
        return startX == endX || startY == endY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BattleshipsGameState

        return board.contentDeepEquals(other.board) &&
                opponentBoard.contentDeepEquals(other.opponentBoard) &&
                shipsSunk == other.shipsSunk
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + opponentBoard.contentDeepHashCode()
        result = 31 * result + shipsSunk.hashCode()
        return result
    }

    // Other game logic functions
    // ...
}