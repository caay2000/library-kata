package com.github.caay2000.librarykata.core.configuration.cqrs

import com.github.caay2000.common.dateprovider.LocalDateProvider
import com.github.caay2000.common.idgenerator.UUIDGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.CreateAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.SearchAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.CreateBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.FindBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.SearchBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.CreateLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.FindLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.FinishLoanController
import com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database.InMemoryAccountRepository
import com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database.InMemoryBookRepository
import com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database.InMemoryLoanRepository
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

        DiKt.register { CreateBookController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FindBookController(DiKt.bind(), DiKt.bind()) }
        DiKt.register { SearchBookController(DiKt.bind(), DiKt.bind()) }

        DiKt.register { FindLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { CreateLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
        DiKt.register { FinishLoanController(DiKt.bind(), DiKt.bind(), DiKt.bind(), DiKt.bind()) }
    }