package de.hwrberlin.sweii.tablegames.session.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "table-games.session")
data class SessionConfiguration(
    val sessionCleanupInterval: Duration,
    val sessionDuration: Duration,
)