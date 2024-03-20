package de.hwrberlin.sweii.tablegames.rest.session

data class UserResponse(
    val id: Long,
    val name: String,
    val host: Boolean,
)