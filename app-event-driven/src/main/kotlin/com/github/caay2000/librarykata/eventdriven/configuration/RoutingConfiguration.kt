package com.github.caay2000.librarykata.eventdriven.configuration

import com.github.caay2000.common.http.get
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.SearchLoanByAccountIdController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.FindBookByIdController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.SearchBookByIsbnController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.FinishLoanController
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
