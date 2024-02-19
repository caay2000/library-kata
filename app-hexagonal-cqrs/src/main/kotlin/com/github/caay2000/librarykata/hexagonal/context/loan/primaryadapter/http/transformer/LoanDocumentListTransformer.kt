package com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentListTransformer(accountRepository: AccountRepository, bookRepository: BookRepository) : Transformer<List<Loan>, JsonApiDocumentList<LoanResource>> {
    private val accountQueryHandler: QueryHandler<FindAccountQuery, FindAccountQueryResponse> = FindAccountQueryHandler(accountRepository)
    private val bookQueryHandler: QueryHandler<FindBookQuery, FindBookQueryResponse> = FindBookQueryHandler(bookRepository)

    override fun invoke(
        value: List<Loan>,
        include: List<String>,
    ): JsonApiDocumentList<LoanResource> {
        val accounts = value.map { accountQueryHandler.invoke(FindAccountQuery(it.accountId)).account }.toSet()
        val books = value.map { bookQueryHandler.invoke(FindBookQuery(it.bookId)).book }.toSet()
        return value.toJsonApiLoanDocumentList(accounts, books, include)
    }
}

fun List<Loan>.toJsonApiLoanDocumentList(
    accounts: Collection<Account> = emptyList(),
    books: Collection<Book> = emptyList(),
    include: Collection<String> = emptyList(),
): JsonApiDocumentList<LoanResource> {
    return JsonApiDocumentList(
        data =
            map {
                it.toJsonApiLoanResource()
            },
        included =
            mapNotNull {
                it.manageLoanIncludes(
                    include = include,
                    account = accounts.firstOrNull { account -> account.id == it.accountId },
                    book = books.firstOrNull { book -> book.id == it.bookId },
                )
            }.flatten().toSet().ifEmpty { null },
        meta = JsonApiMeta(total = size),
    )
}
