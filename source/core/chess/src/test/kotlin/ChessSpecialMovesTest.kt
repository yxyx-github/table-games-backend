import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece
import de.hwrberlin.sweii.tablegames.chess.ChessPieceType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChessSpecialMovesTest {

    @Test
    fun enPassant() {
        val chess = Chess()
        chess.defineWhiteUser(0)

        assertTrue { chess.move(0, 1, 0, 3, 0) }
        assertTrue { chess.move(0, 6, 0, 5, 1) }
        assertTrue { chess.move(0, 3, 0, 4, 0) }
        assertTrue { chess.move(1, 6, 1, 4,1) }
        assertTrue { chess.move(0, 4, 1, 5, 0) }
        assertNull(chess.board[4][1])
    }

    @Test
    fun falseEnPassant() {
        val chess = Chess()
        chess.defineWhiteUser(0)

        assertTrue { chess.move(3, 1, 3, 3, 0) }
        assertTrue { chess.move(2, 6, 2, 4,1) }
        assertTrue { chess.move(3, 3, 3, 4, 0) }
        assertTrue { chess.move(0, 6, 0, 4, 1) }
        assertFalse { chess.move(3, 4, 2, 5, 0) }
        assertEquals(ChessPiece.BLACK_PAWN, chess.board[4][0])
        assertEquals(ChessPiece.BLACK_PAWN, chess.board[4][2])
        assertEquals(ChessPiece.WHITE_PAWN, chess.board[4][3])
    }

    @Test
    fun promotion() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.WHITE_KING
        board[1][7] = ChessPiece.BLACK_PAWN
        board[6][3] = ChessPiece.WHITE_PAWN
        board[6][6] = ChessPiece.BLACK_KING
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertFalse { chess.move(3, 6, 3, 7, 0) }
        assertTrue { chess.promotion(3, 6, 3, 7, ChessPieceType.QUEEN,0) }
        assertEquals(ChessPiece.WHITE_QUEEN, chess.board[7][3])

        assertFalse { chess.move(7, 1, 7, 0, 1) }
        assertFalse { chess.promotion(7, 1, 7, 0, ChessPieceType.PAWN,1) }
        assertTrue { chess.promotion(7, 1, 7, 0, ChessPieceType.KNIGHT,1) }
        assertEquals(ChessPiece.BLACK_KNIGHT, chess.board[0][7])
    }

    @Test
    fun castle() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.WHITE_ROOK
        board[0][4] = ChessPiece.WHITE_KING
        board[0][7] = ChessPiece.WHITE_ROOK
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertTrue { chess.castle(true, 0) }
        assertEquals(ChessPiece.WHITE_ROOK, chess.board[0][5])
        assertEquals(ChessPiece.WHITE_KING, chess.board[0][6])
        assertNull(chess.board[0][4])
        assertNull(chess.board[0][7])
    }

    @Test
    fun castleNotAllowed() {
        val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
        board[0][0] = ChessPiece.WHITE_ROOK
        board[0][1] = ChessPiece.WHITE_KNIGHT
        board[0][4] = ChessPiece.WHITE_KING
        board[0][7] = ChessPiece.WHITE_ROOK
        board[2][7] = ChessPiece.BLACK_BISHOP
        board[6][5] = ChessPiece.WHITE_PAWN
        board[7][0] = ChessPiece.BLACK_ROOK
        board[7][4] = ChessPiece.BLACK_KING
        board[7][7] = ChessPiece.BLACK_ROOK
        val chess: Chess = Chess(board)
        chess.defineWhiteUser(0)

        assertFalse { chess.castle(true, 0) }
        assertFalse { chess.castle(false, 0) }
        assertTrue { chess.move(1, 0, 0, 2, 0) }

        assertFalse { chess.castle(true, 1) }
        assertFalse { chess.castle(false, 1) }
        assertTrue { chess.move(4, 7, 5, 6, 1) }

        assertFalse { chess.castle(true, 0) }
        assertTrue { chess.castle(false, 0) }

        assertFalse { chess.castle(true, 1) }
        assertFalse { chess.castle(false, 1) }
        assertTrue { chess.move(5, 6, 4, 7, 1) }

        assertFalse { chess.castle(true, 0) }
        assertFalse { chess.castle(false, 0) }
        assertTrue { chess.move(0, 2, 1, 0, 0) }

        assertFalse { chess.castle(true, 1) }
        assertFalse { chess.castle(false, 1) }

        assertNull(chess.board[0][0])
        assertNull(chess.board[6][5])
        assertEquals(ChessPiece.WHITE_KING, chess.board[0][2])
        assertEquals(ChessPiece.WHITE_ROOK, chess.board[0][3])
        assertEquals(ChessPiece.WHITE_ROOK, chess.board[0][7])
        assertEquals(ChessPiece.BLACK_ROOK, chess.board[7][0])
        assertEquals(ChessPiece.BLACK_KING, chess.board[7][4])
        assertEquals(ChessPiece.BLACK_ROOK, chess.board[7][7])
    }
}