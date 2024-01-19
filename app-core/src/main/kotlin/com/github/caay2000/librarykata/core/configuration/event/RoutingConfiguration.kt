package com.github.caay2000.librarykata.core.configuration.event

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
            post("/loan/{bookId}", FinishLoanController.documentation) { DiKt.get<FinishLoanController>().invoke(this.call) }
            post("/loan", CreateLoanController.documentation) { DiKt.get<CreateLoanController>().invoke(this.call) }
        }
    }
