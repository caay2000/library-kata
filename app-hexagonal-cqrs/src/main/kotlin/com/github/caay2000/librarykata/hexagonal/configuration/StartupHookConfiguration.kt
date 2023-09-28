package com.github.caay2000.librarykata.hexagonal.configuration

import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import mu.KLogger
import mu.KotlinLogging
import java.util.Optional

val StartupHookConfiguration = createApplicationPlugin(name = "StartupHookConfiguration") {

    val logger: KLogger = KotlinLogging.logger {}
    val welcomeMessage = Optional.ofNullable(this::class.java.getResource("/welcome.txt")?.readText())
        .orElse("Application Started")

    on(MonitoringEvent(ApplicationStarted)) {
        logger.info { welcomeMessage }
    }
}
