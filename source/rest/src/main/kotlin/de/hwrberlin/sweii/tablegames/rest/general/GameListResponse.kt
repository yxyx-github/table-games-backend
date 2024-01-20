package de.hwrberlin.sweii.tablegames.rest.general

data class GameListResponse(
    val games: List<Game>
) {

    data class Game(
        val name: String,
        val minPlayerCount: Int,
        val maxPlayerCount: Int,
    )
}