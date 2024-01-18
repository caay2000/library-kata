package com.github.caay2000.dikt

sealed class DiKtException : RuntimeException {
    constructor(message: String) : super(message)

    data class BeanAlreadyExists(val name: String) : DiKtException("a bean named $name already exists in the context")

    data class BeanNotFound(val bean: String) : DiKtException("bean $bean not found (not registered or unable to instantiate)")

    data class MultipleBeansFound(val type: String) : DiKtException("multiple beans found for type $type")
}
