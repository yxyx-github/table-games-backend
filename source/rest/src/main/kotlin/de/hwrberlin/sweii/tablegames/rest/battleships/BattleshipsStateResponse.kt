package de.hwrberlin.sweii.tablegames.rest.battleships

import de.hwrberlin.sweii.tablegames.battleships.BattleshipsGameState
import de.hwrberlin.sweii.tablegames.battleships.State
data class BattleshipsStateResponse(
    val playerBoard: Array<Array<BattleshipsGameState.ShipStatus>>,
    val opponentBoard: Array<Array<BattleshipsGameState.ShipStatus>>,
    val turn: Long,
    val state: State,
    val winner: Long?,
    val sunkShips: List<Int>
)
