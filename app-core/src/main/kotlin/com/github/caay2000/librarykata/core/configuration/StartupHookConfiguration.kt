package com.github.caay2000.librarykata.core.configuration

import com.github.caay2000.librarykata.core.Architecture
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import mu.KLogger
import mu.KotlinLogging
import java.util.Optional

internal fun startupHookConfiguration(architecture: Architecture) =
    createApplicationPlugin(name = "StartupHookConfiguration") {
        val logger: KLogger = KotlinLogging.logger {}
        val welcomeMessage =
            Optional.ofNullable(this::class.java.getResource("/welcome-${architecture.name.lowercase()}.txt")?.readText())
                .orElse("Application Started")

        on(MonitoringEvent(ApplicationStarted)) {
            logger.info { welcomeMessage }
        }
    }
