package com.github.caay2000.librarykata.hexagonal.configuration

import com.github.caay2000.common.http.get
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.CreateAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.CreateBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.FindBookByIdController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.SearchBookByIsbnController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.SearchBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.CreateLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.FinishLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.SearchLoanByAccountIdController
import io.ktor.server.application.call
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

val RoutingConfiguration = createApplicationPlugin(name = "RoutingConfiguration") {
    application.routing {
        post("/account") { DiKt.get<CreateAccountController>().invoke(this.call) }
        get("/account/{id}") { DiKt.get<FindAccountController>().invoke(this.call) }
        get("/account/{id}/loan") { DiKt.get<SearchLoanByAccountIdController>().invoke(this.call) }

        get("/book", queryParam = "isbn") { DiKt.get<SearchBookByIsbnController>().invoke(this.call) }
        get("/book") { DiKt.get<SearchBookController>().invoke(this.call) }
        get("/book/{id}") { DiKt.get<FindBookByIdController>().invoke(this.call) }
        post("/book") { DiKt.get<CreateBookController>().invoke(this.call) }

        post("/loan") { DiKt.get<CreateLoanController>().invoke(this.call) }
        post("/loan/{bookId}") { DiKt.get<FinishLoanController>().invoke(this.call) }
    }
}
