import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe

/**
 * Only for debugging
 */
fun printBoard(ticTacToe: TicTacToe) {
    val board: StringBuilder = StringBuilder()

    board.appendLine("| --- | 000 | 001 | 002 |")
    for (y in 0..<3) {
        board.append("| 00$y |")
        for (x in 0..<3) {
            val piece: String = ticTacToe.board[x][y].toString()
            board.append(" ${piece.padStart(3, ' ')} |")
        }
        board.appendLine()
    }
    println(board.toString())
}