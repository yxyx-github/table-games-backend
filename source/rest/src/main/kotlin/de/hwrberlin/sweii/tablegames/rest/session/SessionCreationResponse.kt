package de.hwrberlin.sweii.tablegames.rest.session

data class SessionCreationResponse(
    val sessionToken: String,
    val authToken: String,
    val userId: Long
)