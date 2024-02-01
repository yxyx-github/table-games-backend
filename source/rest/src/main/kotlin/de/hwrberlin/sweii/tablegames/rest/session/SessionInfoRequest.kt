package de.hwrberlin.sweii.tablegames.rest.session

data class SessionInfoRequest(
    val sessionToken: String,
    val authToken: String,
    val userId: Long,
)