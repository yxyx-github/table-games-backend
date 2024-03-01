import de.hwrberlin.sweii.tablegames.battleships.ShipStatus


/**
 * Only for debugging
 */
fun printBoard(gameBoard: Array<Array<ShipStatus>>) {
    val board: StringBuilder = StringBuilder()

    board.appendLine("| --- | 000 | 001 | 002 | 003 | 004 | 005 | 006 | 007 | 008 | 009 |")
    for (y in 0..<10) {
        board.append("| 00$y |")
        for (x in 0..<10) {
            val piece: String = when (gameBoard[x][y]) {
                ShipStatus.SHIP -> " S "
                ShipStatus.HIT -> " X "
                ShipStatus.EMPTY -> "   "
                ShipStatus.MISS -> " . "
            }
            board.append(" $piece |")
        }
        board.appendLine()
    }
    println(board.toString())
}