package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import java.util.UUID

object BookIdMother {
    fun random(): BookId = BookId(UUID.randomUUID().toString())
}
