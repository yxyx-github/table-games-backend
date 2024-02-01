package de.hwrberlin.sweii.tablegames.rest.session

import de.hwrberlin.sweii.tablegames.rest.general.GameResponse

data class SessionInfoResponse(
    val game: GameResponse,
    val user: UserResponse
)