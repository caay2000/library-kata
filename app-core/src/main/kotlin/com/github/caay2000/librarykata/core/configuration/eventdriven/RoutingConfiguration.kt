package com.github.caay2000.librarykata.core.configuration.eventdriven

import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.CreateAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.FindAccountController
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.SearchAccountController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.CreateBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.FindBookController
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.SearchBookController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.CreateLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.FindLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.FinishLoanController
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.SearchLoanController
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.server.application.call
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.routing.routing

val RoutingConfiguration =
    createApplicationPlugin(name = "RoutingConfiguration") {
        application.routing {

            get("/account", SearchAccountController.documentation) { DiKt.get<SearchAccountController>().invoke(this.call) }
            get("/account/{id}", FindAccountController.documentation) { DiKt.get<FindAccountController>().invoke(this.call) }
            post("/account", CreateAccountController.documentation) { DiKt.get<CreateAccountController>().invoke(this.call) }

            get("/book", SearchBookController.documentation) { DiKt.get<SearchBookController>().invoke(this.call) }
            get("/book/{id}", FindBookController.documentation) { DiKt.get<FindBookController>().invoke(this.call) }
            post("/book", CreateBookController.documentation) { DiKt.get<CreateBookController>().invoke(this.call) }

            get("/loan/{loanId}", FindLoanController.documentation) { DiKt.get<FindLoanController>().invoke(this.call) }
            get("/loan", SearchLoanController.documentation) { DiKt.get<SearchLoanController>().invoke(this.call) }
            post("/loan/{bookId}", FinishLoanController.documentation) { DiKt.get<FinishLoanController>().invoke(this.call) }
            post("/loan", CreateLoanController.documentation) { DiKt.get<CreateLoanController>().invoke(this.call) }
        }
    }
