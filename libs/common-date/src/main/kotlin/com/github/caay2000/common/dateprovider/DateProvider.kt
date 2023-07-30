package com.github.caay2000.common.dateprovider

import java.time.LocalDate
import java.time.LocalDateTime

interface DateProvider {

    fun date(): LocalDate
    fun dateTime(): LocalDateTime
}
