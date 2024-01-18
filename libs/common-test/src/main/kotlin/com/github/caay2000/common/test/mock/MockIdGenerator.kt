package com.github.caay2000.common.test.mock

import com.github.caay2000.common.idgenerator.IdGenerator
import java.util.UUID

class MockIdGenerator : IdGenerator {
    private var index = 0
    private val responses: MutableList<String> = mutableListOf()

    fun mock(vararg ids: String) {
        ids.forEach { responses.add(it) }
    }

    fun mock(vararg ids: UUID) {
        ids.forEach { responses.add(it.toString()) }
    }

    override fun generate(): String {
        if (index >= responses.size) throw RuntimeException("no mock defined for IdGenerator.generate()")
        val result = responses[index]
        index = index.inc()
        return result
    }
}
