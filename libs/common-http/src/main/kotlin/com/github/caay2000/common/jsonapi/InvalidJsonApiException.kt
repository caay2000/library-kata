package com.github.caay2000.common.jsonapi

data class InvalidJsonApiException(override val message: String) : RuntimeException(message)
