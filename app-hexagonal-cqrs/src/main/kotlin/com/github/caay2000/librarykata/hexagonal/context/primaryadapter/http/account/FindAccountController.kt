package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAccountDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.serializersModuleOf
import kotlinx.serialization.modules.subclass
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class FindAccountController(accountRepository: AccountRepository, loanRepository: LoanRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val accountQueryHandler = FindAccountByIdQueryHandler(accountRepository)
    private val loanQueryHandler = SearchLoanByAccountIdQueryHandler(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!).toString())
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = accountQueryHandler.invoke(FindAccountByIdQuery(accountId))
        val loans = if (jsonApiParams.include.contains("loans")) {
            loanQueryHandler.invoke(SearchLoanByAccountIdQuery(accountId)).value
        } else {
            emptyList()
        }

        val response = queryResult.account.toAccountDocument(loans)

        val json = Json {
            prettyPrint = true
            isLenient = true
            val module = SerializersModule {
                polymorphic(JsonApiResourceAttributes::class) {
                    subclass(LoanDocument.Resource.Attributes::class)
                    subclass(AccountDocument.Resource.Attributes::class)
                }
                serializersModuleOf(UUID::class, UUIDSerializer)
                serializersModuleOf(LocalDate::class, LocalDateSerializer)
                serializersModuleOf(LocalDateTime::class, LocalDateTimeSerializer)
            }
            serializersModule = module
        }
        logger.info { response }
        logger.info { json.encodeToString(response) }

        call.respond(HttpStatusCode.OK, response)
    }
}
