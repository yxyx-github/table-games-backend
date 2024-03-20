package de.hwrberlin.sweii.tablegames.general

enum class Game(
    val minPlayerCount: Int,
    val maxPlayerCount: Int
) {
    BATTLESHIPS(2, 2),
    CHESS(2, 2),
    TIC_TAC_TOE(2, 2),
}