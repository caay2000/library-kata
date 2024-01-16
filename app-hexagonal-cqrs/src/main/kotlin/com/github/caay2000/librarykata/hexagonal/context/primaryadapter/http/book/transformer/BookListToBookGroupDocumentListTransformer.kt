package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

class BookListToBookGroupDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Book>, JsonApiListDocument<BookGroupResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Book>,
        includes: List<String>,
    ): JsonApiListDocument<BookGroupResource> =
        JsonApiListDocument(
            data =
                value.groupBy { it.isbn }
                    .map {
                        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIsbnQuery(it.key.value)).value
                        it.value.toJsonApiDocumentBookGroupResource(loans)
                    },
            meta = JsonApiMeta(total = value.size),
        )
}
