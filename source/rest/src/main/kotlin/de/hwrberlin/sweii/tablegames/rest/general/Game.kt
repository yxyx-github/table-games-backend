package de.hwrberlin.sweii.tablegames.rest.general

enum class Game(
    val minPlayerCount: Int,
    val maxPlayerCount: Int
) {
    TIC_TAC_TOE(2, 2);
}