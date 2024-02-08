import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ChessLegalMovesTest {

    @Test
    fun pawnLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[1][2] = ChessPiece.WHITE_KNIGHT
                initialBoard[3][3] = ChessPiece.WHITE_ROOK
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(2, 1, x, y, null)
            }
        }
        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(true, false, false, false, true, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(true, false, false, false, true, false, false, false),
            arrayOf(false, true, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false)
        )) }
    }

    @Test
    fun knightLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[0][0] = ChessPiece.BLACK_PAWN
                initialBoard[1][2] = ChessPiece.WHITE_KNIGHT
                initialBoard[3][3] = ChessPiece.WHITE_ROOK
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(2, 1, x, y, null)
            }
        }
        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(true, false, false, false, true, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(true, false, false, false, true, false, false, false),
            arrayOf(false, true, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
        )) }
    }


    @Test
    fun bishopLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[3][3] = ChessPiece.WHITE_BISHOP
                initialBoard[6][6] = ChessPiece.BLACK_PAWN
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(3, 3, x, y, null)
            }
        }
        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(true, false, false, false, false, false, true, false),
            arrayOf(false, true, false, false, false, true, false, false),
            arrayOf(false, false, true, false, true, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, true, false, true, false, false, false),
            arrayOf(false, true, false, false, false, true, false, false),
            arrayOf(true, false, false, false, false, false, true, false),
            arrayOf(false, false, false, false, false, false, false, false)
        )) }
    }

    @Test
    fun rookLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[3][3] = ChessPiece.WHITE_ROOK
                initialBoard[1][3] = ChessPiece.BLACK_PAWN
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(3, 3, x, y, null)
            }
        }
        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, true, false, false, false, false),
            arrayOf(false, false, false, true, false, false, false, false),
            arrayOf(true, true, true, false, true, true, true, true),
            arrayOf(false, false, false, true, false, false, false, false),
            arrayOf(false, false, false, true, false, false, false, false),
            arrayOf(false, false, false, true, false, false, false, false),
            arrayOf(false, false, false, true, false, false, false, false)
        )) }
    }

    @Test
    fun queenLegalMoves() {

    }

    @Test
    fun kingLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[1][1] = ChessPiece.BLACK_KING
                initialBoard[3][3] = ChessPiece.WHITE_KING
                initialBoard[5][4] = ChessPiece.BLACK_PAWN
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(3, 3, x, y, null)
            }
        }

        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, true, true, false, false, false),
            arrayOf(false, false, true, false, true, false, false, false),
            arrayOf(false, false, true, false, true, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false)
        )) }
    }

    @Test
    fun kingNoLegalMoves() {
        val result: Array<Array<Boolean>> = Array(8) { Array(8) { true } }

        for (y in 0..<8) {
            for (x in 0..<8) {
                val initialBoard: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
                initialBoard[2][7] = ChessPiece.BLACK_ROOK
                initialBoard[3][1] = ChessPiece.BLACK_PAWN
                initialBoard[3][3] = ChessPiece.WHITE_KING
                initialBoard[4][6] = ChessPiece.BLACK_KNIGHT
                initialBoard[5][2] = ChessPiece.BLACK_ROOK
                initialBoard[5][4] = ChessPiece.BLACK_PAWN
                initialBoard[6][2] = ChessPiece.BLACK_BISHOP
                val chess: Chess = Chess(initialBoard)
                result[y][x] = chess.move(3, 3, x, y, null)
            }
        }

        assertTrue { result.contentDeepEquals(arrayOf(
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false),
            arrayOf(false, false, false, false, false, false, false, false)
        )) }
    }
}