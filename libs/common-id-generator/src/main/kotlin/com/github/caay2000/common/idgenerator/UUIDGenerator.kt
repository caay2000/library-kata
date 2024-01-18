package com.github.caay2000.common.idgenerator

import java.util.UUID

class UUIDGenerator : IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}
