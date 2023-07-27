package com.github.caay2000.projectskeleton.configuration

import com.github.caay2000.common.dateprovider.LocalDateProvider
import com.github.caay2000.common.event.api.AsyncDomainEventBus
import com.github.caay2000.common.event.api.DomainEventBus
import com.github.caay2000.common.event.api.subscribe
import com.github.caay2000.common.idgenerator.AccountNumberGenerator
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.eventbus.EventBus
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.projectskeleton.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.projectskeleton.context.communication.communication.primaryadapter.event.ProcessCommunicationRequestOnUserCreatedEventSubscriber
import com.github.caay2000.projectskeleton.context.communication.communication.primaryadapter.event.SendCommunicationRequestOnCommunicationRequestCreatedEventSubscriber
import com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.communication.LoggingCommunicationRequestEmailSender
import com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.communication.LoggingCommunicationRequestSmsSender
import com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.database.InMemoryCommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.database.InMemoryTemplateRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.primaryadapter.event.CreateContactDetailsOnUserCreatedEventSubscriber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.secondaryadapter.database.InMemoryContactDetailsRepository
import io.ktor.server.application.createApplicationPlugin

val DependencyInjectionConfiguration = createApplicationPlugin(name = "DependencyInjectionConfiguration") {

    DiKt.register { InMemoryDatasource() }
    DiKt.register { InMemoryAccountRepository(DiKt.bind()) }
    DiKt.register { InMemoryContactDetailsRepository(DiKt.bind()) }
    DiKt.register { InMemoryTemplateRepository() }
    DiKt.register { InMemoryCommunicationRequestRepository(DiKt.bind()) }

    DiKt.register(name = "accountNumberGenerator") { AccountNumberGenerator() }
    DiKt.register(name = "uuidGenerator") { UUIDGenerator() }
    DiKt.register { LocalDateProvider() }
//    DiKt.register { LoggingCommunicationProcessor() }

    DiKt.register { LoggingCommunicationRequestSmsSender() }
    DiKt.register { LoggingCommunicationRequestEmailSender() }

    DiKt.register { EventBus(numPartitions = 3) }
    DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
    DiKt.get<DomainEventBus>()
        .subscribe(CreateContactDetailsOnUserCreatedEventSubscriber(DiKt.bind()))
        .subscribe(ProcessCommunicationRequestOnUserCreatedEventSubscriber(DiKt.bind(name = "uuidGenerator"), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()))
        .subscribe(SendCommunicationRequestOnCommunicationRequestCreatedEventSubscriber(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()))

    DiKt.register { CreateAccountController(DiKt.bind(name = "accountNumberGenerator"), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
}
