package com.github.caay2000.projectskeleton.configuration

import com.github.caay2000.common.event.api.AsyncDomainEventBus
import com.github.caay2000.common.event.api.DomainEventBus
import com.github.caay2000.common.event.api.subscribe
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.eventbus.EventBus
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.projectskeleton.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.projectskeleton.context.communication.primaryadapter.event.SendCommunicationOnUserCreatedEventSubscriber
import com.github.caay2000.projectskeleton.context.communication.secondaryadapter.communication.LoggingCommunicationProcessor
import com.github.caay2000.projectskeleton.context.communication.secondaryadapter.database.InMemoryTemplateRepository
import io.ktor.server.application.createApplicationPlugin

val DependencyInjectionConfiguration = createApplicationPlugin(name = "DependencyInjectionConfiguration") {

    DiKt.register { InMemoryDatasource() }
    DiKt.register { InMemoryAccountRepository(DiKt.bind()) }
    DiKt.register { InMemoryTemplateRepository() }

    DiKt.register { UUIDGenerator() }
    DiKt.register { LoggingCommunicationProcessor() }

    DiKt.register { EventBus(numPartitions = 3) }
    DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
    DiKt.get<DomainEventBus>()
        .subscribe(SendCommunicationOnUserCreatedEventSubscriber(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()))

    DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
}
