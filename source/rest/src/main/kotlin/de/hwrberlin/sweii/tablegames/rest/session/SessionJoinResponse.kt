package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.rest.general.GameResponse

data class SessionJoinResponse (
    val authToken: String,
    val user: UserResponse,
    val game: GameResponse,
)