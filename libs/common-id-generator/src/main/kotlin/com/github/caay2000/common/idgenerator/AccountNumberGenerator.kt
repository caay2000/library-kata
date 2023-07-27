package com.github.caay2000.common.idgenerator

class AccountNumberGenerator : IdGenerator {

    private val charPool: List<Char> = ('0'..'9').toList()
    private val accountNumberLength: Int = 9

    override fun generate(): String = (1..accountNumberLength).map { charPool.random() }.joinToString("")
}
