package com.github.caay2000.librarykata.core.configuration.cqrs

import com.github.caay2000.common.date.provider.LocalDateProvider
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.SearchAccountController
import com.github.caay2000.librarykata.hexagonal.context.account.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.FindBookController
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.hexagonal.context.book.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.FindLoanController
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.FinishLoanController
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.SearchLoanController
import com.github.caay2000.librarykata.hexagonal.context.loan.secondaryadapter.database.InMemoryLoanRepository
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

        DiKt.register { CreateAccountController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindAccountController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchAccountController(DiKt.bind(), DiKt.bind()) }

        DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindBookController(DiKt.bind()) }
        DiKt.register { SearchBookController(DiKt.bind(), DiKt.bind()) }

        DiKt.register { FindLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    }
