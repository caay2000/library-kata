package com.github.caay2000.librarykata.core.configuration.eventdriven

import com.github.caay2000.common.dateprovider.LocalDateProvider
import com.github.caay2000.common.event.AsyncDomainEventBus
import com.github.caay2000.common.event.DomainEventBus
import com.github.caay2000.common.event.subscribe
import com.github.caay2000.common.eventbus.EventBus
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.SearchAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.FindBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.eventdriven.context.book.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.account.primaryadapter.event.CreateAccountOnAccountCreatedEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.FindLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.FinishLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.secondaryadapter.database.InMemoryLoanRepository
import com.github.caay2000.memorydb.InMemoryDatasource
import io.ktor.server.application.createApplicationPlugin

val DependencyInjectionConfiguration =
    createApplicationPlugin(name = "DependencyInjectionConfiguration") {

        DiKt.register { InMemoryDatasource() }
        DiKt.register { UUIDGenerator() }
        DiKt.register { LocalDateProvider() }

        DiKt.register { InMemoryAccountRepository(DiKt.bind()) }
        DiKt.register { InMemoryBookRepository(DiKt.bind()) }
        DiKt.register { InMemoryLoanRepository(DiKt.bind()) }

        DiKt.register { com.github.caay2000.librarykata.eventdriven.context.loan.account.secondaryadapter.InMemoryAccountRepository(DiKt.bind()) }

        DiKt.register { EventBus(numPartitions = 3) }
        DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
        DiKt.get<DomainEventBus>()
            .subscribe(CreateAccountOnAccountCreatedEventSubscriber(DiKt.bind()))

        DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindAccountController(DiKt.bind()) }
        DiKt.register { SearchAccountController(DiKt.bind()) }

        DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindBookController(DiKt.bind()) }
        DiKt.register { SearchBookController(DiKt.bind()) }

        DiKt.register { FindLoanController(DiKt.bind()) }
        DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind()) }

//        DiKt.register { InMemoryDatasource() }
//        // Account Context
//        DiKt.register { InMemoryAccountRepository(DiKt.bind()) }
//        DiKt.register { InMemoryAccountBookRepository(DiKt.bind()) }
//
//        // Book Context
//        DiKt.register { InMemoryBookRepository(DiKt.bind()) }
//        DiKt.register { InMemoryBookLoanRepository(DiKt.bind()) }
//
//        // Loan Context
//        DiKt.register { InMemoryLoanRepository(DiKt.bind()) }
//        DiKt.register { InMemoryUserRepository(DiKt.bind()) }
//        DiKt.register { InMemoryBookRepositoryLoanContext(DiKt.bind()) }
//
//        DiKt.register { UUIDGenerator() }
//        DiKt.register { LocalDateProvider() }
//
//        DiKt.register { EventBus(numPartitions = 3) }
//        DiKt.register { AsyncDomainEventBus(DiKt.bind()) }
//        DiKt.get<DomainEventBus>()
//            .subscribe(CreateUserOnAccountCreatedEventSubscriber(DiKt.bind()))
//            .subscribe(CreateAccountBookOnBookCreatedEventSubscriber(DiKt.bind()))
//            .subscribe(CreateLoanBookOnBookCreatedEventSubscriber(DiKt.bind()))
//            .subscribe(UpdateBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.get()))
//            .subscribe(UpdateBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.get()))
//            .subscribe(StartLoanOnLoanCreatedEventSubscriber(DiKt.get()))
//            .subscribe(FinishLoanOnLoanFinishedEventSubscriber(DiKt.get()))
//            .subscribe(UpdateLoanBookAvailabilityOnLoanCreatedEventSubscriber(DiKt.get()))
//            .subscribe(UpdateLoanBookAvailabilityOnLoanFinishedEventSubscriber(DiKt.get()))
//            .subscribe(IncreaseUserCurrentLoansOnLoanCreatedEventSubscriber(DiKt.get()))
//            .subscribe(DecreaseUserCurrentLoansOnLoanFinishedEventSubscriber(DiKt.get()))
//
//        DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
//        DiKt.register { FindAccountController(DiKt.bind(), DiKt.bind()) }

//        DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
//        DiKt.register { FindBookByIdController(DiKt.bind()) }
//        DiKt.register { SearchBookController(DiKt.bind()) }
//        DiKt.register { SearchBookByIsbnController(DiKt.bind()) }
//        DiKt.register { SearchLoanController(DiKt.bind(), DiKt.bind()) }
//
//        DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
//        DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    }
