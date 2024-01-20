package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.general.Game

data class SessionCreationRequest(
    val host: String,
    val game: Game
)

