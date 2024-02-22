package de.hwrberlin.sweii.tablegames.rest.battleships

import de.hwrberlin.sweii.tablegames.battleships.State
import de.hwrberlin.sweii.tablegames.battleships.ShipStatus

data class BattleshipsStateResponse(
    val playerBoard: Array<Array<ShipStatus>>,
    val opponentBoard: Array<Array<ShipStatus>>,
    val yourTurn: Boolean,
    val gameState: State,
    val winner: Long?
)


