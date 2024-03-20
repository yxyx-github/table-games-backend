package de.hwrberlin.sweii.tablegames.rest.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "table-games.rest")
data class RestConfiguration(
    val sseTimeout: Duration,
)