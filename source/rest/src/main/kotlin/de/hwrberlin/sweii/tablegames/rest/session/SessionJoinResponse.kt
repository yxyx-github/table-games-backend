package de.hwrberlin.sweii.tablegames.rest.session

data class SessionJoinResponse (
    val authToken: String,
    val userID: Long,
)