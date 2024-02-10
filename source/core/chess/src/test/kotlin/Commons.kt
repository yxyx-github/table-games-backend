import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.chess.ChessPiece

/**
 * Only for debugging
 */
fun printBoard(chess: Chess) {
    val board: StringBuilder = StringBuilder()

    board.appendLine("| --- | 000 | 001 | 002 | 003 | 004 | 005 | 006 | 007 |")
    for (y in 0..<8) {
        board.append("| 00$y |")
        for (x in 0..<8) {
            val piece: String = when (chess.board[y][x]) {
                ChessPiece.WHITE_KING -> "K_W"
                ChessPiece.WHITE_QUEEN -> "Q_W"
                ChessPiece.WHITE_ROOK -> "R_W"
                ChessPiece.WHITE_BISHOP -> "B_W"
                ChessPiece.WHITE_KNIGHT -> "N_W"
                ChessPiece.WHITE_PAWN -> "P_W"
                ChessPiece.BLACK_KING -> "K_B"
                ChessPiece.BLACK_QUEEN -> "Q_B"
                ChessPiece.BLACK_ROOK -> "R_B"
                ChessPiece.BLACK_BISHOP -> "B_B"
                ChessPiece.BLACK_KNIGHT -> "N_B"
                ChessPiece.BLACK_PAWN -> "P_B"
                null -> "   "
            }
            board.append(" $piece |")
        }
        board.appendLine()
    }
    println(board.toString())
}