import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece
import de.hwrberlin.sweii.tablegames.chess.ChessPieceColor
import de.hwrberlin.sweii.tablegames.chess.State
import org.junit.jupiter.api.Test
import kotlin.math.ceil
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChessStateTest {

    @Test
    fun checkmate() {
        val chess: Chess = Chess()
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)

        assertTrue { chess.move(5, 1, 5, 2, 0) }
        assertTrue { chess.move(4, 6, 4, 5, 1) }
        assertTrue { chess.move(6, 1, 6, 3, 0) }
        assertTrue { chess.move(3, 7, 7, 3, 1) }

        assertEquals(State.DECIDED, chess.state)
        assertEquals(ChessPieceColor.BLACK, chess.winner)
        assertEquals(1, chess.winner(1))
    }

    @Test
    fun seventyFiveMovesRule() {
        val chess: Chess = Chess()
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)

        for (i in 0..<18) {
            assertTrue { chess.move(1, 0, 0, 2, 0) }
            assertTrue { chess.move(1, 7, 0, 5, 1) }

            assertTrue { chess.move(0, 2, 1, 0, 0) }
            assertTrue { chess.move(0, 5, 1, 7, 1) }
            assertEquals(State.RUNNING, chess.state)
        }

        assertTrue { chess.move(1, 0, 0, 2, 0) }
        assertTrue { chess.move(1, 7, 0, 5, 1) }
        assertTrue { chess.move(0, 2, 1, 0, 0) }

        assertEquals(State.DRAW, chess.state)

        assertFalse { chess.move(0, 5, 1, 7, 1) }
    }

    @Test
    fun stalemate() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[2][0] = ChessPiece.WHITE_PAWN
        board[3][0] = ChessPiece.BLACK_PAWN
        board[5][5] = ChessPiece.WHITE_BISHOP
        board[6][5] = ChessPiece.WHITE_KING
        board[6][7] = ChessPiece.BLACK_KING
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)
        assertTrue { chess.move(5, 5, 6, 6, 0) }
        assertEquals(State.DRAW, chess.state)

        for (y in 0..<8) {
            for (x in 0..<8) {
                assertFalse { chess.move(0, 3, x, y, 1) }
                assertFalse { chess.move(7, 6, x, y, 1) }
            }
        }
    }

    @Test
    fun deadBoardKings() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.BLACK_KING
        board[6][6] = ChessPiece.BLACK_PAWN
        board[7][7] = ChessPiece.WHITE_KING
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)
        assertTrue { chess.move(7, 7, 6, 6, 0) }
        assertEquals(State.DRAW, chess.state)
    }

    @Test
    fun deadBoardKingsWithKnight() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.BLACK_KING
        board[0][7] = ChessPiece.WHITE_KNIGHT
        board[6][6] = ChessPiece.BLACK_PAWN
        board[7][7] = ChessPiece.WHITE_KING
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)
        assertTrue { chess.move(7, 7, 6, 6, 0) }
        assertEquals(State.DRAW, chess.state)
    }

    @Test
    fun deadBoardKingsWithBishop() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.BLACK_KING
        board[0][7] = ChessPiece.BLACK_BISHOP
        board[6][6] = ChessPiece.BLACK_PAWN
        board[7][7] = ChessPiece.WHITE_KING
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertEquals(State.RUNNING, chess.state)
        assertTrue { chess.move(7, 7, 6, 6, 0) }
        assertEquals(State.DRAW, chess.state)
    }
}