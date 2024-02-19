package com.github.caay2000.librarykata.core.configuration.eventdriven

import com.github.caay2000.common.date.provider.LocalDateProvider
import com.github.caay2000.common.event.AsyncDomainEventBus
import com.github.caay2000.common.event.DomainEventBus
import com.github.caay2000.common.event.init
import com.github.caay2000.common.event.subscribe
import com.github.caay2000.common.eventbus.EventBus
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.common.query.register
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event.CreateLoanOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event.DecreaseLoansOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event.IncreaseLoansOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event.RemoveLoanOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.SearchAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer.AccountResourceQueryBusHandler
import com.github.caay2000.librarykata.eventdriven.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.event.UpdateBookAvailabilityOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.event.UpdateBookAvailabilityOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.FindBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer.BookResourceQueryBusHandler
import com.github.caay2000.librarykata.eventdriven.context.book.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account.CreateAccountOnAccountCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.book.CreateBookOnBookCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.FindLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.FinishLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.SearchLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.LoanResourceQueryBusHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.secondaryadapter.database.InMemoryLoanRepository
import com.github.caay2000.memorydb.InMemoryDatasource
import io.ktor.server.application.createApplicationPlugin
import com.github.caay2000.librarykata.eventdriven.context.account.secondaryadapter.database.InMemoryLoanRepository as InMemoryLoanRepositoryAccountContext
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account.DecreaseLoansOnLoanFinishedEventSubscriber as LoanDecreaseLoansOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account.IncreaseLoansOnLoanCreatedEventSubscriber as LoanIncreaseLoansOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.book.UpdateBookAvailabilityOnLoanCreatedEventSubscriber as UpdateLoanContextBookAvailabilityOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.book.UpdateBookAvailabilityOnLoanFinishedEventSubscriber as UpdateLoanContextBookAvailabilityOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.secondaryadapter.database.InMemoryAccountRepository as InMemoryAccountRepositoryLoanContext
import com.github.caay2000.librarykata.eventdriven.context.loan.secondaryadapter.database.InMemoryBookRepository as InMemoryBookRepositoryLoanContext

val DependencyInjectionConfiguration =
    createApplicationPlugin(name = "DependencyInjectionConfiguration") {

        DiKt.register { InMemoryDatasource() }
        DiKt.register { UUIDGenerator() }
        DiKt.register { LocalDateProvider() }

        DiKt.register { InMemoryAccountRepository(DiKt.bind()) }
        DiKt.register { InMemoryBookRepository(DiKt.bind()) }
        DiKt.register { InMemoryLoanRepository(DiKt.bind()) }

        DiKt.register { InMemoryLoanRepositoryAccountContext(DiKt.bind()) }
        DiKt.register { InMemoryAccountRepositoryLoanContext(DiKt.bind()) }
        DiKt.register { InMemoryBookRepositoryLoanContext(DiKt.bind()) }

        DiKt.register { EventBus(numPartitions = 3) }
        DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
        DiKt.get<DomainEventBus>()
            .subscribe(CreateLoanOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(RemoveLoanOnLoanFinishedEventSubscriber(DiKt.bind()))
            .subscribe(IncreaseLoansOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(DecreaseLoansOnLoanFinishedEventSubscriber(DiKt.bind()))
            .subscribe(CreateAccountOnAccountCreatedEventSubscriber(DiKt.bind()))
            .subscribe(CreateBookOnBookCreatedEventSubscriber(DiKt.bind()))
            .subscribe(LoanIncreaseLoansOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(LoanDecreaseLoansOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(UpdateLoanContextBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(UpdateLoanContextBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.bind()))
            .subscribe(UpdateBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.bind()))
            .subscribe(UpdateBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.bind()))
            .init()

        DiKt.register { ResourceQueryBus() }
        DiKt.get<ResourceQueryBus>()
            .register(AccountResourceQueryBusHandler(DiKt.bind(), DiKt.bind()))
            .register(BookResourceQueryBusHandler(DiKt.bind()))
            .register(LoanResourceQueryBusHandler(DiKt.bind()))

        DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }

        DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindBookController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchBookController(DiKt.bind()) }

        DiKt.register { FindLoanController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchLoanController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    }
