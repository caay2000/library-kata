package com.github.caay2000.projectskeleton.configuration

import com.github.caay2000.common.dateprovider.LocalDateProvider
import com.github.caay2000.common.event.AsyncDomainEventBus
import com.github.caay2000.common.event.DomainEventBus
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.eventbus.EventBus
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.projectskeleton.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.FindBookByIdController
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.SearchBookByIsbnController
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.projectskeleton.context.book.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.projectskeleton.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.projectskeleton.context.loan.secondaryadapter.database.InMemoryLoanRepository
import com.github.caay2000.projectskeleton.context.loan.secondaryadapter.database.InMemoryUserRepository
import io.ktor.server.application.createApplicationPlugin
import com.github.caay2000.projectskeleton.context.loan.secondaryadapter.database.InMemoryBookRepository as InMemoryBookRepositoryLoanContext

val DependencyInjectionConfiguration = createApplicationPlugin(name = "DependencyInjectionConfiguration") {

    DiKt.register { InMemoryDatasource() }
    // Account Context
    DiKt.register { InMemoryAccountRepository(DiKt.bind()) }

    // Book Context
    DiKt.register { InMemoryBookRepository(DiKt.bind()) }

    // Loan Context
    DiKt.register { InMemoryLoanRepository(DiKt.bind()) }
    DiKt.register { InMemoryUserRepository(DiKt.bind()) }
    DiKt.register { InMemoryBookRepositoryLoanContext(DiKt.bind()) }

    DiKt.register { UUIDGenerator() }
    DiKt.register { LocalDateProvider() }

    DiKt.register { EventBus(numPartitions = 3) }
    DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
    DiKt.get<DomainEventBus>()
//        .subscribe(CreateContactDetailsOnUserCreatedEventSubscriber(DiKt.bind()))
//        .subscribe(ProcessCommunicationRequestOnUserCreatedEventSubscriber(DiKt.bind(name = "uuidGenerator"), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()))
//        .subscribe(SendCommunicationRequestOnCommunicationRequestCreatedEventSubscriber(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()))

    DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    DiKt.register { FindAccountController(DiKt.bind()) }

    DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    DiKt.register { FindBookByIdController(DiKt.bind()) }
    DiKt.register { SearchBookController(DiKt.bind()) }
    DiKt.register { SearchBookByIsbnController(DiKt.bind()) }

    DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
}
