package com.github.caay2000.common.dateprovider

import java.time.LocalDate
import java.time.LocalDateTime

class LocalDateProvider : DateProvider {

    override fun date(): LocalDate = LocalDate.now()
    override fun dateTime(): LocalDateTime = LocalDateTime.now()
}
