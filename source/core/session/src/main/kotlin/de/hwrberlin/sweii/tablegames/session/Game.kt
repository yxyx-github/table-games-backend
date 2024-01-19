package de.hwrberlin.sweii.tablegames.session

enum class Game(
    val minPlayerCount: Int,
    val maxPlayerCount: Int
) {
    TIC_TAC_TOE(2, 2);
}