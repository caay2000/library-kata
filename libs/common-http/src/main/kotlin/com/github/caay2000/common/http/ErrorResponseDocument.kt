package com.github.caay2000.common.http

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDocument(val message: String)
