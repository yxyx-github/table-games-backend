import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece
import de.hwrberlin.sweii.tablegames.chess.State
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChessGamesTest {

    @Test
    fun startingPosition() {
        val chess: Chess = Chess()
        assertEquals(ChessPiece.WHITE_ROOK, chess.board[0][0])
        assertEquals(ChessPiece.WHITE_KNIGHT, chess.board[0][1])
        assertEquals(ChessPiece.WHITE_BISHOP, chess.board[0][2])
        assertEquals(ChessPiece.WHITE_QUEEN, chess.board[0][3])
        assertEquals(ChessPiece.WHITE_KING, chess.board[0][4])
        assertEquals(ChessPiece.WHITE_BISHOP, chess.board[0][5])
        assertEquals(ChessPiece.WHITE_KNIGHT, chess.board[0][6])
        assertEquals(ChessPiece.WHITE_ROOK, chess.board[0][7])
        for (x in 0..7) assertEquals(ChessPiece.WHITE_PAWN, chess.board[1][x])
        for (x in 0..7) assertEquals(ChessPiece.BLACK_PAWN, chess.board[6][x])
        assertEquals(ChessPiece.BLACK_ROOK, chess.board[7][0])
        assertEquals(ChessPiece.BLACK_KNIGHT, chess.board[7][1])
        assertEquals(ChessPiece.BLACK_BISHOP, chess.board[7][2])
        assertEquals(ChessPiece.BLACK_QUEEN, chess.board[7][3])
        assertEquals(ChessPiece.BLACK_KING, chess.board[7][4])
        assertEquals(ChessPiece.BLACK_BISHOP, chess.board[7][5])
        assertEquals(ChessPiece.BLACK_KNIGHT, chess.board[7][6])
        assertEquals(ChessPiece.BLACK_ROOK, chess.board[7][7])
    }

    /**
     * Example game taken from https://en.wikibooks.org/wiki/Chess/Sample_chess_game
     */
    @Test
    fun exampleGame() {
        val chess: Chess = Chess()
        chess.defineWhiteUser(0)

        assertTrue { chess.move(4, 1, 4, 3, 0) }
        assertTrue { chess.move(4, 6, 4, 4, 1) }
        assertTrue { chess.move(6, 0, 5, 2, 0) }
        assertTrue { chess.move(5, 6, 5, 5, 1) }
        assertTrue { chess.move(5, 2, 4, 4, 0) }
        assertTrue { chess.move(5, 5, 4, 4, 1) }
        assertTrue { chess.move(3, 0, 7, 4, 0) }
        assertTrue { chess.move(4, 7, 4, 6, 1) }
        assertTrue { chess.move(7, 4, 4, 4, 0) }
        assertTrue { chess.move(4, 6, 5, 6, 1) }
        assertTrue { chess.move(5, 0, 2, 3, 0) }
        assertTrue { chess.move(3, 6, 3, 4, 1) }
        assertTrue { chess.move(2, 3, 3, 4, 0) }
        assertTrue { chess.move(5, 6, 6, 5, 1) }
        assertTrue { chess.move(7, 1, 7, 3, 0) }
        assertTrue { chess.move(7, 6, 7, 4, 1) }
        assertTrue { chess.move(3, 4, 1, 6, 0) }
        assertTrue { chess.move(2, 7, 1, 6, 1) }
        assertTrue { chess.move(4, 4, 5, 4, 0) }
        assertTrue { chess.move(6, 5, 7, 5, 1) }
        assertTrue { chess.move(3, 1, 3, 3, 0) }
        assertTrue { chess.move(6, 6, 6, 4, 1) }
        assertTrue { chess.move(5, 4, 5, 6, 0) }
        assertTrue { chess.move(3, 7, 4, 6, 1) }
        assertTrue { chess.move(7, 3, 6, 4, 0) }
        assertTrue { chess.move(4, 6, 6, 4, 1) }
        assertTrue { chess.move(7, 0, 7, 4, 0) }

        assertEquals(State.DECIDED, chess.state)
        assertEquals(0, chess.winner(1  ))
    }
}