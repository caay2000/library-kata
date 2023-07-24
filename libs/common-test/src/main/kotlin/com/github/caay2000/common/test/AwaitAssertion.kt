package com.github.caay2000.common.test

import org.awaitility.kotlin.await
import java.util.concurrent.TimeUnit

fun awaitAssertion(
    atLeast: Long = 0,
    atMost: Long = 2000,
    pollInterval: Long = 100,
    block: () -> Unit,
) = await
    .atLeast(atLeast, TimeUnit.MILLISECONDS)
    .atMost(atMost, TimeUnit.MILLISECONDS)
    .pollInterval(pollInterval, TimeUnit.MILLISECONDS)
    .untilAsserted(block)
