package com.github.caay2000.librarykata.hexagonalarrow.context.book.mother

import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookIsbn
import java.util.UUID

object BookIsbnMother {
    fun random(): BookIsbn = BookIsbn(UUID.randomUUID().toString())
}
