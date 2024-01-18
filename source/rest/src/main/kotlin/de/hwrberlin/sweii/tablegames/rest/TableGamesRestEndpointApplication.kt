package de.hwrberlin.sweii.tablegames.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@ComponentScan("de.hwrberlin")
@ConfigurationPropertiesScan("de.hwrberlin")
@EnableJpaRepositories("de.hwrberlin")
@EntityScan("de.hwrberlin.sweii.tablegames.session.enitity")
@EnableScheduling
@SpringBootApplication
open class TableGamesRestEndpointApplication

fun main(args: Array<String>) {
    SpringApplication(TableGamesRestEndpointApplication::class.java)
        .run(*args)
}

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}