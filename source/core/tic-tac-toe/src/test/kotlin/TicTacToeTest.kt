import de.hwrberlin.sweii.tablegames.tictactoe.State
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TicTacToeTest {

    @Test
    fun draw() {
        val ticTacToe: TicTacToe = TicTacToe()
        assertEquals(State.RUNNING, ticTacToe.state)
        assertTrue { ticTacToe.move(0, 0, 1) }
        assertTrue { ticTacToe.move(1, 1, 2) }
        assertTrue { ticTacToe.move(2, 2, 1) }
        assertTrue { ticTacToe.move(0, 1, 2) }
        assertTrue { ticTacToe.move(2, 1, 1) }
        assertTrue { ticTacToe.move(2, 0, 2) }
        assertTrue { ticTacToe.move(1, 0, 1) }
        assertTrue { ticTacToe.move(1, 2, 2) }
        assertTrue { ticTacToe.move(0, 2, 1) }
        assertEquals(State.DRAW, ticTacToe.state)
        assertEquals(null, ticTacToe.winner(listOf(1, 2)))
    }

    @Test
    fun win() {
        val ticTacToe: TicTacToe = TicTacToe()
        assertEquals(State.RUNNING, ticTacToe.state)
        assertTrue { ticTacToe.move(0, 0, 1) }
        assertTrue { ticTacToe.move(1, 1, 2) }
        assertTrue { ticTacToe.move(2, 2, 1) }
        assertTrue { ticTacToe.move(0, 1, 2) }
        assertTrue { ticTacToe.move(2, 1, 1) }
        assertTrue { ticTacToe.move(2, 0, 2) }
        assertTrue { ticTacToe.move(1, 0, 1) }
        assertTrue { ticTacToe.move(0, 2, 2) }
        assertEquals(State.DECIDED, ticTacToe.state)
        assertEquals(2, ticTacToe.winner(listOf(1, 2)))
    }

    @Test
    fun placeWhenNotRunning() {
        val ticTacToe: TicTacToe = TicTacToe()
        assertEquals(State.RUNNING, ticTacToe.state)
        assertTrue { ticTacToe.move(0, 0, 1) }
        assertTrue { ticTacToe.move(1, 1, 2) }
        assertTrue { ticTacToe.move(2, 2, 1) }
        assertTrue { ticTacToe.move(0, 1, 2) }
        assertTrue { ticTacToe.move(2, 1, 1) }
        assertTrue { ticTacToe.move(2, 0, 2) }
        assertTrue { ticTacToe.move(1, 0, 1) }
        assertTrue { ticTacToe.move(0, 2, 2) }
        assertEquals(State.DECIDED, ticTacToe.state)
        assertEquals(2, ticTacToe.winner(listOf(1, 2)))
        assertFalse { ticTacToe.move(0, 2, 1) }
    }

    @Test
    fun placeTwiceInARow() {
        val ticTacToe: TicTacToe = TicTacToe()
        assertEquals(State.RUNNING, ticTacToe.state)
        assertTrue { ticTacToe.move(0, 0, 1) }
        assertFalse { ticTacToe.move(1, 1, 1) }
        assertEquals(State.RUNNING, ticTacToe.state)
        assertEquals(null, ticTacToe.winner(listOf(1, 2)))
    }

    @Test
    fun placeOnNonEmptyTile() {
        val ticTacToe: TicTacToe = TicTacToe()
        assertEquals(State.RUNNING, ticTacToe.state)
        assertTrue { ticTacToe.move(0, 0, 1) }
        assertFalse { ticTacToe.move(0, 0, 2) }
        assertEquals(State.RUNNING, ticTacToe.state)
        assertEquals(null, ticTacToe.winner(listOf(1, 2)))
    }
}