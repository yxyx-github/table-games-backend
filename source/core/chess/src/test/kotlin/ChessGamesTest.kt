import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.State
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChessGamesTest {

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