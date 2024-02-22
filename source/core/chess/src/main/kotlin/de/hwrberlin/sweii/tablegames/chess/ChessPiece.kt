package de.hwrberlin.sweii.tablegames.chess

enum class ChessPiece(
    val color: ChessPieceColor,
    val type: ChessPieceType,
) {
    WHITE_KING(ChessPieceColor.WHITE, ChessPieceType.KING),
    WHITE_QUEEN(ChessPieceColor.WHITE, ChessPieceType.QUEEN),
    WHITE_ROOK(ChessPieceColor.WHITE, ChessPieceType.ROOK),
    WHITE_BISHOP(ChessPieceColor.WHITE, ChessPieceType.BISHOP),
    WHITE_KNIGHT(ChessPieceColor.WHITE, ChessPieceType.KNIGHT),
    WHITE_PAWN(ChessPieceColor.WHITE, ChessPieceType.PAWN),
    BLACK_KING(ChessPieceColor.BLACK, ChessPieceType.KING),
    BLACK_QUEEN(ChessPieceColor.BLACK, ChessPieceType.QUEEN),
    BLACK_ROOK(ChessPieceColor.BLACK, ChessPieceType.ROOK),
    BLACK_BISHOP(ChessPieceColor.BLACK, ChessPieceType.BISHOP),
    BLACK_KNIGHT(ChessPieceColor.BLACK, ChessPieceType.KNIGHT),
    BLACK_PAWN(ChessPieceColor.BLACK, ChessPieceType.PAWN),
}