package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import kotlinx.serialization.encodeToString

object BookDocumentMother {
    internal fun random(book: Book = BookMother.random()) = book.toJsonApiBookDocument()

    internal fun random(
        book: Book = BookMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
    ) = book.toJsonApiBookDocument(loans)

    internal fun json(book: Book = BookMother.random()) = jsonMapper.encodeToString(book.toJsonApiBookDocument())

    internal fun json(
        book: Book = BookMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
        included: List<String> = emptyList(),
    ) = jsonMapper.encodeToString(book.toJsonApiBookDocument(loans, included.map { it.uppercase() }))

//    internal fun json(account: Account = AccountMother.random()) = jsonMapper.encodeToString(account.toJsonApiDocument())
//
//    internal fun json(
//        account: Account = AccountMother.random(),
//        loan: Loan,
//    ) = jsonMapper.encodeToString(account.toJsonApiDocument(listOf(loan)))
//
//    internal fun json(
//        account: Account = AccountMother.random(),
//        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
//        included: List<String> = emptyList(),
//    ) = jsonMapper.encodeToString(account.toJsonApiDocument(loans, included.map { it.uppercase() }))
//
}
