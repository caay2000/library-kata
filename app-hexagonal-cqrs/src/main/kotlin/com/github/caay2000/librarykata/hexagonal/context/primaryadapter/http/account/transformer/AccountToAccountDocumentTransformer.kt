package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transfomer
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoansByAccountIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAccountDocument

class AccountToAccountDocumentTransformer(loanRepository: LoanRepository) : Transfomer<Account, AccountDocument> {

    private val loanQueryHandler: QueryHandler<SearchLoanByAccountIdQuery, SearchLoanByAccountIdQueryResponse> = SearchLoansByAccountIdQueryHandler(loanRepository)

    override fun invoke(value: Account, includes: List<String>): AccountDocument {
        val loans = includes.shouldProcess(FindAccountController.Included.LOANS) {
            loanQueryHandler.invoke(SearchLoanByAccountIdQuery(value.id)).value
        }
        return value.toAccountDocument(loans)
    }
}
