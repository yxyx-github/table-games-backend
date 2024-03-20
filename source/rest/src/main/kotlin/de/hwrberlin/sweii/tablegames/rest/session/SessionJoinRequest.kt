package de.hwrberlin.sweii.tablegames.rest.session

data class SessionJoinRequest(
    val sessionToken: String,
    val name: String,
)