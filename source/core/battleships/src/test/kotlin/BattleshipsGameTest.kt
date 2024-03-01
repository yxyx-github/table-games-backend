import de.hwrberlin.sweii.tablegames.battleships.Battleships
import de.hwrberlin.sweii.tablegames.battleships.ShipStatus
import de.hwrberlin.sweii.tablegames.battleships.ShipType
import de.hwrberlin.sweii.tablegames.battleships.State
import org.junit.jupiter.api.Test
import kotlin.test.*

class BattleshipsGameTest {

    @Test
    fun testEqualShips() {
        val battleships = Battleships()
        battleships.definePlayerOne(1)
        assertEquals(battleships.playerOne.shipsToPlace, battleships.playerTwo.shipsToPlace)
    }

    @Test
    fun placeShipsOutOfBounds() {
        val battleships = Battleships()
        battleships.definePlayerOne(1)
        for (ship: ShipType in listOf(ShipType.CARRIER, ShipType.SUBMARINE, ShipType.CRUISER, ShipType.BATTLESHIP, )) {
            for (userId: Int in 0..1) {
                assertFalse { battleships.placeShip(10, 10, ship, true, 1) }
                assertFalse { battleships.placeShip(9, 9, ship, true, 1) }
                assertFalse { battleships.placeShip(9, 9, ship, false, 1) }
                assertFalse { battleships.placeShip(9, 0, ship, true, 1) }
            }
        }
        assertEquals(battleships.playerOne.shipsToPlace, battleships.playerTwo.shipsToPlace)
    }



    @Test
    fun placeShipsOverlapping() {
        val battleships = Battleships()
        battleships.definePlayerOne(1)
        battleships.placeShip(0, 5, ShipType.BATTLESHIP, true, 1)

        assertFalse{ battleships.placeShip(0, 5, ShipType.CRUISER, true, 1) }
        assertFalse { battleships.placeShip(2, 3, ShipType.CARRIER, false, 1) }
    }

    @Test
    fun placeShipTwice() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var y: Int = 0
        for (ship in ShipType.entries) {
            battleships.placeShip(0, y, ship, true, 0)
            y++
        }

        y = 0
        for (ship in ShipType.entries) {
            assertFalse { battleships.placeShip(0, y, ship, true, 0) }
            y++
        }
    }

    @Test
    fun placeShipsBordering() {
        for (ship in ShipType.entries) {
            val battleships = Battleships()
            battleships.definePlayerOne(0)

            battleships.placeShip(0, 0, ship, true, 0)

            for (ship1 in ShipType.entries) {
                assertFalse { battleships.placeShip(0, 1, ship1, true, 0) }
            }
        }
    }

    @Test
    fun placeShipsHorizontal() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var y: Int = 1
        for (ship in ShipType.entries) {
            battleships.placeShip(1, y, ship, true, 0)
            y += 2
        }

        assertTrue(battleships.playerOne.shipsToPlace.isEmpty())
        assertEquals(battleships.state, State.PLACING)

        y = 1
        for (ship in ShipType.entries) {
            battleships.placeShip(1, y, ship, true, 1)
            y += 2
        }

        assertTrue { battleships.playerTwo.shipsToPlace.isEmpty() }
        assertEquals(battleships.state, State.ATTACKING)
    }

    @Test
    fun placeShipsVertical() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var x: Int = 1
        for (ship in ShipType.entries) {
            battleships.placeShip(x, 1, ship, false, 0)
            x += 2
        }

        assertTrue(battleships.playerOne.shipsToPlace.isEmpty())
        assertEquals(battleships.state, State.PLACING)

        x = 1
        for (ship in ShipType.entries) {
            battleships.placeShip(x, 1, ship, false, 1)
            x += 2
        }

        assertTrue { battleships.playerTwo.shipsToPlace.isEmpty() }
        assertEquals(battleships.state, State.ATTACKING)
    }

    @Test
    fun placeShipsMixed() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        assertTrue { battleships.placeShip(1, 1, ShipType.DESTROYER, true, 0) }
        assertTrue { battleships.placeShip(1, 3, ShipType.SUBMARINE, false, 0)}
        assertTrue { battleships.placeShip(3, 1, ShipType.CRUISER, true, 0)}
        assertTrue { battleships.placeShip(8, 1, ShipType.BATTLESHIP, false, 0)}
        assertTrue { battleships.placeShip(4, 8, ShipType.CARRIER, true, 0)}

        assertTrue { battleships.placeShip(1, 1, ShipType.DESTROYER, true, 1) }
        assertTrue { battleships.placeShip(1, 3, ShipType.SUBMARINE, false, 1)}
        assertTrue { battleships.placeShip(3, 1, ShipType.CRUISER, true, 1)}
        assertTrue { battleships.placeShip(8, 1, ShipType.BATTLESHIP, false, 1)}
        assertTrue { battleships.placeShip(4, 8, ShipType.CARRIER, true, 1)}

        assertTrue { battleships.playerTwo.shipsToPlace.isEmpty() }
        assertTrue { battleships.playerOne.shipsToPlace.isEmpty() }
        assertNotEquals(battleships.state, State.PLACING)
        assertEquals(battleships.state, State.ATTACKING)
    }

    @Test
    fun attackTooEarly() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 0)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 0)
            assertFalse { battleships.attack(0, 0, 0) }
            help += 2
        }

        assertTrue { battleships.playerOne.shipsToPlace.isEmpty() }

        help = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 1)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 1)
            assertFalse { battleships.attack(0, 0, 0) }
            help += 2
        }

        assertTrue { battleships.playerTwo.shipsToPlace.isEmpty() }
        assertEquals(battleships.state, State.ATTACKING)
        assertTrue { battleships.attack(1, 0, 1) }
    }

    @Test
    fun attackOutOfBounds() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 0)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 0)
            help += 2
        }

        help = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 1)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 1)
            help += 2
        }

        assertFalse { battleships.attack(10,10,1) }
    }

    @Test
    fun attackShips() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 0)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 0)
            help += 2
        }

        help = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 1)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 1)
            help += 2
        }

        assertTrue { battleships.attack(9,9,1) }
        assertTrue { battleships.attack(0,9,1) }

        assertTrue { battleships.attack(9,9,0) }
        assertTrue { battleships.attack(0,9,0) }
    }

    @Test
    fun placeAfterAttack() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 0)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 0)
            help += 2
        }

        help = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 1)
        for (ship in listOf(ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE, ShipType.CARRIER)) {
            battleships.placeShip(0, help, ship, true, 1)
            help += 2
        }

        battleships.attack(9,9,1)
        battleships.attack(0,9,1)

        battleships.attack(9,9,0)
        battleships.attack(0,9,0)

        assertFalse { battleships.placeShip(0,0, ShipType.SUBMARINE, true, 0) }
        assertFalse { battleships.placeShip(0,0, ShipType.SUBMARINE, true, 1) }
    }

    @Test
    fun sinkShips() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 0)
        for (ship in listOf(ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE )) {
            battleships.placeShip(0, help, ship, true, 0)
            help += 2
        }

        help = 0
        battleships.placeShip(9,9,ShipType.DESTROYER, true, 1)
        for (ship in listOf(ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE )) {
            battleships.placeShip(0, help, ship, true, 1)
            help += 2
        }

        battleships.attack(9,9, 1)
        battleships.attack(9,0, 1)
        assertEquals(battleships.playerTwo.shipsSunk[0], ShipType.DESTROYER)

        battleships.attack(9,9, 0)
        battleships.attack(9,0, 0)
        assertEquals(battleships.playerOne.shipsSunk[0], ShipType.DESTROYER)

    }

    @Test
    fun borderingIsMarkedWhenSunk() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        battleships.placeShip(1, 1, ShipType.SUBMARINE, true, 0)
        battleships.placeShip(5, 1, ShipType.BATTLESHIP, true, 0)
        battleships.placeShip(1, 4, ShipType.DESTROYER, true, 0)
        battleships.placeShip(4, 4, ShipType.CARRIER, true, 0)
        battleships.placeShip(1, 9, ShipType.CRUISER, true, 0)

        battleships.placeShip(1, 1, ShipType.SUBMARINE, true, 1)
        battleships.placeShip(5, 1, ShipType.BATTLESHIP, true, 1)
        battleships.placeShip(1, 4, ShipType.DESTROYER, true, 1)
        battleships.placeShip(4, 4, ShipType.CARRIER, true, 1)
        battleships.placeShip(1, 9, ShipType.CRUISER, true, 1)


        battleships.attack(1, 1, 1)
        battleships.attack(2, 1, 1)

        battleships.attack(5, 1, 1)
        battleships.attack(6, 1, 1)
        battleships.attack(7, 1, 1)
        battleships.attack(8, 1, 1)

        battleships.attack(1, 4, 1)

        battleships.attack(4, 4, 1)
        battleships.attack(5, 4, 1)
        battleships.attack(6, 4, 1)
        battleships.attack(7, 4, 1)
        battleships.attack(8, 4, 1)

        for (x in 0 until 10) {
            for (y in 0 until 6 ){
                assertContains(listOf(ShipStatus.HIT, ShipStatus.MISS), battleships.playerOne.board[x][y])
            }
        }
    }

    @Test
    fun winner() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var y: Int = 0
        for (ship in ShipType.entries) {
            assertTrue {battleships.placeShip(1, y, ship, true, 0) }
            y += 2
        }

        y = 0
        for (ship in ShipType.entries) {
            assertTrue { battleships.placeShip(2, y, ship, true, 1) }
            y += 2
        }

        //Attacking Destroyer
        assertTrue { battleships.attack(1, 0, 1) }

        assertContains(battleships.playerTwo.shipsSunk, ShipType.DESTROYER)

        //Attacking Submarine
        assertTrue { battleships.attack(1, 2, 1) }
        assertTrue { battleships.attack(2, 2, 1) }

        assertContains(battleships.playerTwo.shipsSunk, ShipType.SUBMARINE)

        //Attacking Cruiser
        assertTrue { battleships.attack(0+1, 4, 1) }
        assertTrue { battleships.attack(1+1, 4, 1) }
        assertTrue { battleships.attack(2+1, 4, 1) }

        assertContains(battleships.playerTwo.shipsSunk, ShipType.CRUISER)

        //Attacking Battleship
        assertTrue { battleships.attack(0+1, 6, 1) }
        assertTrue { battleships.attack(1+1, 6, 1) }
        assertTrue { battleships.attack(2+1, 6, 1) }
        assertTrue { battleships.attack(3+1, 6, 1) }

        assertContains(battleships.playerTwo.shipsSunk, ShipType.BATTLESHIP)

        //Attacking Carrier
        assertTrue { battleships.attack(0+1, 8, 1) }
        assertTrue { battleships.attack(1+1, 8, 1) }
        assertTrue { battleships.attack(2+1, 8, 1) }
        assertTrue { battleships.attack(3+1, 8, 1) }
        assertTrue { battleships.attack(4+1, 8, 1) }

        assertContains(battleships.playerTwo.shipsSunk, ShipType.CARRIER)

        for (ship in ShipType.entries) {
            assertContains(battleships.playerTwo.shipsSunk, ship)
        }

        assertEquals(State.DECIDED, battleships.state)
        assertEquals(1, battleships.winner(1))
    }
}