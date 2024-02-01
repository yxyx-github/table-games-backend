package de.hwrberlin.sweii.tablegames.rest.general

data class GameResponse(
    val name: String,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
)