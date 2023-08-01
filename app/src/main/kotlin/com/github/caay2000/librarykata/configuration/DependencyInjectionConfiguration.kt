package com.github.caay2000.librarykata.configuration

import com.github.caay2000.common.dateprovider.LocalDateProvider
import com.github.caay2000.common.event.AsyncDomainEventBus
import com.github.caay2000.common.event.DomainEventBus
import com.github.caay2000.common.event.subscribe
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.eventbus.EventBus
import com.github.caay2000.librarykata.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.librarykata.context.book.primaryadapter.event.FinishLoanOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.context.book.primaryadapter.event.StartLoanOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.context.book.primaryadapter.event.UpdateBookAvailabilityOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.context.book.primaryadapter.event.UpdateBookAvailabilityOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.context.book.primaryadapter.http.FindBookByIdController
import com.github.caay2000.librarykata.context.book.primaryadapter.http.SearchBookByIsbnController
import com.github.caay2000.librarykata.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.context.book.primaryadapter.http.SearchLoanByUserIdController
import com.github.caay2000.librarykata.context.book.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.CreateBookOnBookCreatedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.CreateUserOnAccountCreatedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.DecreaseUserCurrentLoansOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.IncreaseUserCurrentLoansOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.UpdateLoanBookAvailabilityOnLoanCreatedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.event.UpdateLoanBookAvailabilityOnLoanFinishedEventSubscriber
import com.github.caay2000.librarykata.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.context.loan.primaryadapter.http.FinishLoanController
import com.github.caay2000.librarykata.context.loan.secondaryadapter.database.InMemoryLoanRepository
import com.github.caay2000.librarykata.context.loan.secondaryadapter.database.InMemoryUserRepository
import com.github.caay2000.memorydb.InMemoryDatasource
import io.ktor.server.application.createApplicationPlugin
import com.github.caay2000.librarykata.context.book.secondaryadapter.database.InMemoryLoanRepository as InMemoryBookLoanRepository
import com.github.caay2000.librarykata.context.loan.secondaryadapter.database.InMemoryBookRepository as InMemoryBookRepositoryLoanContext

val DependencyInjectionConfiguration = createApplicationPlugin(name = "DependencyInjectionConfiguration") {

    DiKt.register { InMemoryDatasource() }
    // Account Context
    DiKt.register { InMemoryAccountRepository(DiKt.bind()) }

    // Book Context
    DiKt.register { InMemoryBookRepository(DiKt.bind()) }
    DiKt.register { InMemoryBookLoanRepository(DiKt.bind()) }

    // Loan Context
    DiKt.register { InMemoryLoanRepository(DiKt.bind()) }
    DiKt.register { InMemoryUserRepository(DiKt.bind()) }
    DiKt.register { InMemoryBookRepositoryLoanContext(DiKt.bind()) }

    DiKt.register { UUIDGenerator() }
    DiKt.register { LocalDateProvider() }

    DiKt.register { EventBus(numPartitions = 3) }
    DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
    DiKt.get<DomainEventBus>()
        .subscribe(CreateUserOnAccountCreatedEventSubscriber(DiKt.bind()))
        .subscribe(CreateBookOnBookCreatedEventSubscriber(DiKt.bind()))
        .subscribe(UpdateBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.get()))
        .subscribe(UpdateBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.get()))
        .subscribe(StartLoanOnLoanCreatedEventSubscriber(DiKt.get()))
        .subscribe(FinishLoanOnLoanFinishedEventSubscriber(DiKt.get()))
        .subscribe(UpdateLoanBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.get()))
        .subscribe(UpdateLoanBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.get()))
        .subscribe(IncreaseUserCurrentLoansOnLoanCreatedEventSubscriber(DiKt.get()))
        .subscribe(DecreaseUserCurrentLoansOnLoanFinishedEventSubscriber(DiKt.get()))

    DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    DiKt.register { FindAccountController(DiKt.bind()) }

    DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    DiKt.register { FindBookByIdController(DiKt.bind()) }
    DiKt.register { SearchBookController(DiKt.bind()) }
    DiKt.register { SearchBookByIsbnController(DiKt.bind()) }
    DiKt.register { SearchLoanByUserIdController(DiKt.bind(), DiKt.bind()) }

    DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
}
