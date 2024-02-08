package de.hwrberlin.sweii.tablegames.chess

import de.hwrberlin.sweii.tablegames.general.GameState
import kotlin.math.abs

class Chess(
    // [y][x]
    val board: Array<Array<ChessPiece?>> = arrayOf(
        arrayOf(
            ChessPiece.WHITE_ROOK,
            ChessPiece.WHITE_KNIGHT,
            ChessPiece.WHITE_BISHOP,
            ChessPiece.WHITE_QUEEN,
            ChessPiece.WHITE_KING,
            ChessPiece.WHITE_BISHOP,
            ChessPiece.WHITE_KNIGHT,
            ChessPiece.WHITE_ROOK
        ),
        Array(8) { ChessPiece.WHITE_PAWN },
        Array(8) { null },
        Array(8) { null },
        Array(8) { null },
        Array(8) { null },
        Array(8) { ChessPiece.BLACK_PAWN },
        arrayOf(
            ChessPiece.BLACK_ROOK,
            ChessPiece.BLACK_KNIGHT,
            ChessPiece.BLACK_BISHOP,
            ChessPiece.BLACK_QUEEN,
            ChessPiece.BLACK_KING,
            ChessPiece.BLACK_BISHOP,
            ChessPiece.BLACK_KNIGHT,
            ChessPiece.BLACK_ROOK
        ),
    )

) : GameState {

    var state: State = State.RUNNING
        private set

    // first y-coordinate, second: x-coordinate
    private var enPassantable: Pair<Int, Int>? = null

    private var movesSinceCapture = 0

    private var whiteUserId: Long? = null

    private var lastTurn: ChessPieceColor = ChessPieceColor.BLACK


    fun move(startX: Int, startY: Int, targetX: Int, targetY: Int, userId: Long?): Boolean {
        if (userId != null && whiteUserId == null && lastTurn == ChessPieceColor.BLACK) whiteUserId = userId;

        if (startX !in 0..<8 || startY !in 0..<8 || targetX !in 0..<8 || targetY !in 0..<8 || board[startY][startX] == null || board[startY][startX]?.color == lastTurn || state != State.RUNNING) return false
        if (!moveIsLegal(startX, startY, targetX, targetY)) return false

        val thisTurn: ChessPieceColor = board[startY][startX]?.color ?: return false
        val capturedPiece: ChessPiece? = board[targetY][targetX]
        board[targetY][targetX] = board[startY][startX]
        board[startY][startX] = null

        if (enPassantable?.first == targetY && enPassantable?.second == targetX) {
            val y: Int = targetY + if (lastTurn == ChessPieceColor.WHITE) (1) else (-1)
            board[y][targetX] = null
        }

        if (board[targetY][targetX]?.type == ChessPieceType.PAWN && abs(targetY - startY) >= 2) {
            val y: Int = startY + if (thisTurn == ChessPieceColor.WHITE) (1) else (-1)
            enPassantable = Pair(y, targetX)
        } else {
            enPassantable = null
        }

        if (capturedPiece == null) movesSinceCapture++ else movesSinceCapture = 0
        state = calculateGameState(lastTurn)

        lastTurn = thisTurn;
        return true;
    }

    private fun moveIsLegal(startX: Int, startY: Int, targetX: Int, targetY: Int): Boolean {
        if (!moveIsValid(startX, startY, targetX, targetY)) return false
        val color: ChessPieceColor = board[startY][startX]!!.color

        val capturedPiece: ChessPiece? = board[targetY][targetX]
        board[targetY][targetX] = board[startY][startX]
        board[startY][startX] = null

        val checked: Boolean =  isChecked(color)
        board[startY][startX] = board[targetY][targetX]
        board[targetY][targetX] = capturedPiece
        return !checked
    }

    /**
     * Checks if a move in is valid while only looking at the start and the target,
     * this means that moves that leave the king in check aren't necessarily invalid.
     *
     * If you are looking for a check that a move is legal in the context of the current game,
     * then you need to use the moveIsLegal method.
     */
    private fun moveIsValid(startX: Int, startY: Int, targetX: Int, targetY: Int): Boolean {
        if (startX !in 0..<8 || startY !in 0..<8 || targetX !in 0..<8 || targetY !in 0..<8 || (startY == targetY && startX == targetX)) return false
        if (board[targetY][targetX]?.type == ChessPieceType.KING || board[targetY][targetX]?.color == board[startY][startX]?.color) return false

        return when (board[startY][startX]?.type) {
            ChessPieceType.KING -> abs(targetX - startX) in 0..1 && abs(targetY - startY) in 0..1

            ChessPieceType.QUEEN -> isValidStraightMove(startX, startY, targetX, targetY) ||
                    isValidDiagonalMove(startX, startY, targetX, targetY)

            ChessPieceType.ROOK -> isValidStraightMove(startX, startY, targetX, targetY)
            ChessPieceType.BISHOP -> isValidDiagonalMove(startX, startY, targetX, targetY)

            ChessPieceType.KNIGHT -> (abs(targetX - startX) == 2 && abs(targetY - startY) == 1) ||
                    (abs(targetX - startX) == 1 && abs(targetY - startY) == 2)

            ChessPieceType.PAWN -> isValidPawnMove(startX, startY, targetX, targetY)

            null -> false
        }
    }

    private fun isValidPawnMove(startX: Int, startY: Int, targetX: Int, targetY: Int): Boolean {
        return (board[startY][startX]?.color == ChessPieceColor.WHITE &&
                ((startX == targetX && startY + 1 == targetY && board[targetY][targetX] == null) ||
                        (startY == 1 && startX == targetX && startY + 2 == targetY && board[targetY - 1][targetX] == null && board[targetY][targetX] == null) ||
                        ((startX + 1 == targetX || startX - 1 == targetX) && startY + 1 == targetY && (board[targetY][targetX]?.color == ChessPieceColor.BLACK || (enPassantable?.first == targetY && enPassantable?.second == targetX)))))

                || (board[startY][startX]?.color == ChessPieceColor.BLACK &&
                ((startX == targetX && startY - 1 == targetY && board[targetY][targetX] == null) ||
                        (startY == 6 && startX == targetX && startY - 2 == targetY && board[targetY + 1][targetX] == null && board[targetY][targetX] == null) ||
                        ((startX + 1 == targetX || startX - 1 == targetX) && startY - 1 == targetY && (board[targetY][targetX]?.color == ChessPieceColor.WHITE || (enPassantable?.first == targetY && enPassantable?.second == targetX)))))
    }

    private fun isValidStraightMove(startX: Int, startY: Int, targetX: Int, targetY: Int): Boolean {
        if (startX == targetX) {
            val from: Int = if (startY < targetY) startY + 1 else targetY + 1
            val to: Int = if (startY < targetY) targetY else startY
            for (y in from..<to) {
                if (board[y][startX] != null) return false
            }
            return board[startY][startX]?.color != board[targetY][targetX]?.color
        } else if (startY == targetY) {
            val from: Int = if (startX < targetX) startX + 1 else targetX + 1
            val to: Int = if (startX < targetX) targetX else startX
            for (x in from..<to) {
                if (board[startY][x] != null) return false
            }
            return board[startY][startX]?.color != board[targetY][targetX]?.color
        }
        return false;
    }

    private fun isValidDiagonalMove(startX: Int, startY: Int, targetX: Int, targetY: Int): Boolean {
        if (abs(targetY - startY) != abs(targetX - startX)) return false
        val xStep = if (startX < targetX) 1 else -1
        val yStep = if (startY < targetY) 1 else -1

        for (i in 1..<abs(targetY - startY)) {
            if (board[startY + i * yStep][startX + i * xStep] != null) return false
        }
        return board[startY][startX]?.color != board[targetY][targetX]?.color
    }

    private fun isChecked(color: ChessPieceColor): Boolean {
        // if there is no king it can not be checked (edge-case that shouldn't happen under normal conditions)
        val pos: Pair<Int, Int> = getKingPosition(color) ?: return false

        return checkedByPawn(color, pos) ||
                checkedByKing(color, pos) ||
                checkedByKnight(color, pos) ||
                checkedDiagonally(color, pos) ||
                checkedStraightly(color, pos)
    }

    private fun getKingPosition(color: ChessPieceColor): Pair<Int, Int>? {
        for (y in 0..<8) {
            for (x in 0..<8) {
                if (board[y][x]?.type == ChessPieceType.KING && board[y][x]?.color == color) return Pair(y, x);
            }
        }
        return null
    }

    /**
     * Does not look for pawns or kings
     */
    private fun checkedDiagonally(color: ChessPieceColor, pos: Pair<Int, Int>): Boolean {

        fun checkDiagonal(calculateY: (offset: Int) -> Int, calculateX: (offset: Int) -> Int): Boolean {
            var offset: Int = 1;
            while (calculateY(offset) in 0..<8 && calculateX(offset) in 0..<8) {
                val piece: ChessPiece? = board[calculateY(offset)][calculateX(offset)]
                if (piece?.color == color || piece?.type in arrayOf(
                        ChessPieceType.KING,
                        ChessPieceType.PAWN,
                        ChessPieceType.ROOK,
                        ChessPieceType.KNIGHT
                    )
                ) {
                    return false
                } else if (piece?.type in arrayOf(ChessPieceType.QUEEN, ChessPieceType.BISHOP)) {
                    return true
                }
                offset++
            }
            return false
        }

        return checkDiagonal({ pos.first + it }, { pos.second + it }) ||
                checkDiagonal({ pos.first - it }, { pos.second - it }) ||
                checkDiagonal({ pos.first + it }, { pos.second - it }) ||
                checkDiagonal({ pos.first - it }, { pos.second + it })
    }

    private fun checkedByPawn(color: ChessPieceColor, pos: Pair<Int, Int>): Boolean {
        return when (color) {
            ChessPieceColor.WHITE -> try { board[pos.first + 1][pos.second + 1] == ChessPiece.BLACK_PAWN } catch (_: IndexOutOfBoundsException) { false } ||
                    try { board[pos.first + 1][pos.second - 1] == ChessPiece.BLACK_PAWN } catch (_: IndexOutOfBoundsException) { false }

            ChessPieceColor.BLACK -> try { board[pos.first - 1][pos.second + 1] == ChessPiece.WHITE_PAWN } catch (_: IndexOutOfBoundsException) { false } ||
                    try { board[pos.first - 1][pos.second - 1] == ChessPiece.WHITE_PAWN } catch (_: IndexOutOfBoundsException) { false }
        }
    }

    private fun checkedByKing(color: ChessPieceColor, pos: Pair<Int, Int>): Boolean {
        for (yOffset in -1..1) {
            for (xOffset in -1..1) {
                if (yOffset + pos.first !in 0..<8 || xOffset + pos.second !in 0..<8) continue
                val piece: ChessPiece? = board[pos.first + yOffset][pos.second + xOffset]
                if (piece?.type == ChessPieceType.KING && piece.color != color) return true
            }
        }
        return false
    }

    /**
     * Does not look for PAWNS or Kings
     */
    private fun checkedStraightly(color: ChessPieceColor, pos: Pair<Int, Int>): Boolean {

        fun checkStraight(calculateY: (offset: Int) -> Int, calculateX: (offset: Int) -> Int): Boolean {
            var offset: Int = 1;
            while (calculateY(offset) in 0..<8 && calculateX(offset) in 0..<8) {
                val piece: ChessPiece? = board[calculateY(offset)][calculateX(offset)]
                if (piece?.color == color || piece?.type in arrayOf(
                        ChessPieceType.KING,
                        ChessPieceType.PAWN,
                        ChessPieceType.KNIGHT,
                        ChessPieceType.BISHOP
                    )
                ) {
                    return false
                } else if (piece?.type in arrayOf(ChessPieceType.QUEEN, ChessPieceType.ROOK)) {
                    return true
                }
                offset++
            }
            return false
        }

        return checkStraight({ pos.first }, { pos.second + it }) ||
                checkStraight({ pos.first }, { pos.second - it }) ||
                checkStraight({ pos.first + it }, { pos.second }) ||
                checkStraight({ pos.first - it }, { pos.second })
    }

    private fun checkedByKnight(color: ChessPieceColor, pos: Pair<Int, Int>): Boolean {
        val predicate: (yOffset: Int, xOffset: Int) -> Boolean = { yOffset, xOffset ->
            pos.first + yOffset in 0..<8 &&
                    pos.second + xOffset in 0..<8 &&
                    board[pos.first + yOffset][pos.second + xOffset]?.color != color &&
                    board[pos.first + yOffset][pos.second + xOffset]?.type == ChessPieceType.KNIGHT
        }

        return predicate(2, 1) ||
                predicate(2, -1) ||
                predicate(1, 2) ||
                predicate(1, -2) ||
                predicate(-1, 2) ||
                predicate(-1, -2) ||
                predicate(-2, 1) ||
                predicate(-2, -1)
    }

    private fun calculateGameState(nextTurn: ChessPieceColor): State {
        if (isDraw(nextTurn)) return State.DRAW
        if (calculateWinner(nextTurn) != null) return State.DECIDED
        return State.RUNNING
    }

    private fun isDraw(nextTurn: ChessPieceColor): Boolean {
        return movesSinceCapture >= 75 || isDeadPosition() || isStalemate(nextTurn)
    }

    //TODO: CHECK FOR CHECKMATE
    private fun calculateWinner(nextTurn: ChessPieceColor): ChessPieceColor? {
        return null
    }

    private fun isStalemate(color: ChessPieceColor): Boolean {
        for (y in 0..<8) {
            for (x in 0..<8) {
                if (board[y][x]?.color == color && noLegalMoves(y, x)) return false
            }
        }
        return true
    }

    /**
     * Only checks a few common dead positions as it is not feasible to determine if the current board is dead in a timely manner
     */
    private fun isDeadPosition(): Boolean {
        val piecesLeft: Map<ChessPiece, Int> = board.flatten().filterNotNull().groupingBy { it }.eachCount()
        return (piecesLeft[ChessPiece.WHITE_KING] ?: 0) == 1 && (piecesLeft[ChessPiece.BLACK_KING] ?: 0) == 1 &&
                (piecesLeft.size == 2 ||
                        (piecesLeft.size == 3 && ((piecesLeft[ChessPiece.WHITE_BISHOP]?: 0) == 1) || (piecesLeft[ChessPiece.BLACK_BISHOP]?: 0) == 1) ||
                        (piecesLeft.size == 3 && ((piecesLeft[ChessPiece.WHITE_KNIGHT]?: 0) == 1) || (piecesLeft[ChessPiece.WHITE_KNIGHT]?: 0) == 1)
                        )
    }

    private fun noLegalMoves(y: Int, x: Int): Boolean {
        if (board[y][x] == null) return true
        for (targetY in 0..<8) {
            for (targetX in 0..<8) {
                if (moveIsLegal(x, y, targetX, targetY)) return false
            }
        }
        return true
    }

}