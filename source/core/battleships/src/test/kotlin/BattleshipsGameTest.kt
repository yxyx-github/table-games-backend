import de.hwrberlin.sweii.tablegames.battleships.Battleships
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

        var help: Int = 0
        for (ship in ShipType.entries) {
            battleships.placeShip(0, help, ship, true, 0)
            help++
        }

        help = 0
        for (ship in ShipType.entries) {
            assertFalse { battleships.placeShip(0, help, ship, true, 0) }
            help++
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
    fun placeShips() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var help: Int = 0
        for (ship in ShipType.entries) {
            battleships.placeShip(0, help, ship, true, 0)
            help += 2
        }

        assertTrue(battleships.playerOne.shipsToPlace.isEmpty())
        assertEquals(battleships.state, State.PLACING)

        help = 0
        for (ship in ShipType.entries) {
            battleships.placeShip(0, help, ship, true, 1)
            help += 2
        }

        assertTrue { battleships.playerTwo.shipsToPlace.isEmpty() }
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
    fun winner() {
        val battleships = Battleships()
        battleships.definePlayerOne(0)

        var y: Int = 0
        for (ship in ShipType.entries) {
            assertTrue {battleships.placeShip(0, y, ship, true, 0) }
            y += 2
        }

        y = 0
        for (ship in ShipType.entries) {
            assertTrue { battleships.placeShip(0, y, ship, true, 1) }
            y += 2
        }

        y = 0
        for (ship in ShipType.entries) {
            for (x in 0 until  ship.size ) {
                println("Ship being tested: " + ship.name)
                println("x: " + x.toString())
                println("y: " + y.toString())
                assertTrue { battleships.attack(x, y, 1) }
            }
            y += 2
        }


        for (ship in ShipType.entries) {
            println("Checking for")
            println(ship)
            assertContains(battleships.playerTwo.shipsSunk, ship)
        }


    }
}