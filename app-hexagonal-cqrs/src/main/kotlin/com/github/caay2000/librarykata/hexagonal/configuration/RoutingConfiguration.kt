package com.github.caay2000.librarykata.hexagonal.configuration

import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.CreateAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.CreateBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.FindBookByIdController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.SearchBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.CreateLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.FindLoanController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.FinishLoanController
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.server.application.call
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.routing.routing

val RoutingConfiguration = createApplicationPlugin(name = "RoutingConfiguration") {
    application.routing {

        post("/account", CreateAccountController.documentation) { DiKt.get<CreateAccountController>().invoke(this.call) }
        get("/account/{id}", FindAccountController.documentation) { DiKt.get<FindAccountController>().invoke(this.call) }

//        get("/book", queryParam = "isbn") { DiKt.get<SearchBookByIsbnController>().invoke(this.call) }
        get("/book", SearchBookController.documentation) { DiKt.get<SearchBookController>().invoke(this.call) }
        get("/book/{id}", FindBookByIdController.documentation) { DiKt.get<FindBookByIdController>().invoke(this.call) }
        post("/book", CreateBookController.documentation) { DiKt.get<CreateBookController>().invoke(this.call) }

        post("/loan") { DiKt.get<CreateLoanController>().invoke(this.call) }
        get("/loan/{loanId}") { DiKt.get<FindLoanController>().invoke(this.call) }
        post("/loan/{bookId}") { DiKt.get<FinishLoanController>().invoke(this.call) }
    }
}
