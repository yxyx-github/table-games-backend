package de.hwrberlin.sweii.tablegames.battleships

data class BattleshipsActionRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val action: BattleshipsActionType,
    val x: Int,
    val y: Int,
    val shipSize: Int? = null,
    val isHorizontal: Boolean? = null
)
