package com.github.caay2000.common.test.mock

import com.github.caay2000.common.dateprovider.DateProvider
import java.time.LocalDate
import java.time.LocalDateTime

class MockDateProvider : DateProvider {

    private var dateIndex = 0
    private var datetimeIndex = 0
    private val dateMocks: MutableList<LocalDate> = mutableListOf()
    private val datetimeMocks: MutableList<LocalDateTime> = mutableListOf()

    fun mock(vararg dates: LocalDate) {
        dates.forEach { dateMocks.add(it) }
    }

    fun mock(vararg datetimes: LocalDateTime) {
        datetimes.forEach { datetimeMocks.add(it) }
    }

    override fun date(): LocalDate {
        if (dateIndex >= dateMocks.size) throw RuntimeException("no mock defined for DateProvider.date()")
        val result = dateMocks[dateIndex]
        dateIndex = dateIndex.inc()
        return result
    }

    override fun dateTime(): LocalDateTime {
        if (datetimeIndex >= datetimeMocks.size) throw RuntimeException("no mock defined for DateProvider.date()")
        val result = datetimeMocks[datetimeIndex]
        datetimeIndex = datetimeIndex.inc()
        return result
    }
}
