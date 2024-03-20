package de.hwrberlin.sweii.tablegames.battleships

import java.io.Serializable

data class PlayerState(
    val board: Array<Array<ShipStatus>> = Array(10) { Array(10) { ShipStatus.EMPTY } },
    val opponentBoard: Array<Array<ShipStatus>> = Array(10) { Array(10) { ShipStatus.EMPTY } },
    val shipsSunk: MutableList<ShipType> = mutableListOf(),
    val shipsToPlace: MutableList<ShipType> = ShipType.entries.toMutableList()
):Serializable