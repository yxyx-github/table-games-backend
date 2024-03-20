package de.hwrberlin.sweii.tablegames.rest.session

data class SessionCloseRequest(
    val sessionToken: String,
    val authToken: String
)