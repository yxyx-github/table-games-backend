import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece
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
}