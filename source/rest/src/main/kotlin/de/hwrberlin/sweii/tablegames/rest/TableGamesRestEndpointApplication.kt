package de.hwrberlin.sweii.tablegames.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan

@ComponentScan("de.hwrberlin")
@ConfigurationPropertiesScan("de.hwrberlin")
@SpringBootApplication
open class TableGamesRestEndpointApplication

fun main(args: Array<String>) {
    SpringApplication(TableGamesRestEndpointApplication::class.java)
        .run(*args)
}

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}