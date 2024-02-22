package de.hwrberlin.sweii.tablegames.rest.battleships

import de.hwrberlin.sweii.tablegames.battleships.ShipType

data class PlaceShipRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
    val x: Int,
    val y: Int,
    val shipType: ShipType,
    val isHorizontal: Boolean
)