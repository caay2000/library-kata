package com.github.caay2000.librarykata.hexagonal.configuration

import com.github.caay2000.dikt.DiKt
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent

val ShutdownHookConfiguration =
    createApplicationPlugin(name = "ShutdownHookConfiguration") {
        on(MonitoringEvent(ApplicationStopping)) { application ->
            application.environment.monitor.unsubscribe(ApplicationStarted) {}
            application.environment.monitor.unsubscribe(ApplicationStopped) {}
            DiKt.clear()
        }
    }
