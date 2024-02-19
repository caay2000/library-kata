package com.github.caay2000.common.eventbus

import com.github.caay2000.common.test.awaitAssertion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class EventBusTest {
    @AfterEach
    fun tearDown() {
        @OptIn(ExperimentalCoroutinesApi::class)
        Dispatchers.resetMain()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun `subscriber receive the published event`() =
        runTest {
            val stringSubscriber = StringSubscriber()
            val event = StringTestEvent(value = "hi")

            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    val sut = EventBus(this)
                    sut.subscribe(stringSubscriber)
                    sut.init()

                    sut.publish(event)
                }
            job.cancel()
            awaitAssertion {
                assertThat(stringSubscriber.events).containsExactly(event)
            }
        }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun `multiple subscribers receive the published event`() =
        runTest {
            val stringSubscriber1 = StringSubscriber()
            val stringSubscriber2 = StringSubscriber()
            val event = StringTestEvent(value = "hi")

            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    val sut = EventBus(this)
                    sut.subscribe(stringSubscriber1)
                    sut.subscribe(stringSubscriber2)
                    sut.init()

                    sut.publish(event)
                }

            job.cancel()
            awaitAssertion {
                assertThat(stringSubscriber1.events).containsExactly(event)
                assertThat(stringSubscriber2.events).containsExactly(event)
            }
        }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun `subscribers receive just their requested events`() =
        runTest {
            val stringSubscriber = StringSubscriber()
            val intSubscriber = IntSubscriber()
            val stringEvent = StringTestEvent(value = "hi")
            val intEvent = IntTestEvent(value = 1)

            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    val sut = EventBus(this)
                    sut.subscribe(stringSubscriber)
                    sut.subscribe(intSubscriber)
                    sut.init()

                    sut.publish(stringEvent)
                    sut.publish(intEvent)
                }

            job.cancel()
            awaitAssertion {
                assertThat(stringSubscriber.events).containsExactly(stringEvent)
                assertThat(intSubscriber.events).containsExactly(intEvent)
            }
        }

    @Disabled
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun `different partitions receive different aggregates`() =
        runTest {
            val stringSubscriber = StringSubscriber()
            val stringEvent1 = StringTestEvent(value = "hi1", aggregateId = "0")
            val stringEvent2 = StringTestEvent(value = "hi2", aggregateId = "0")
            val stringEvent3 = StringTestEvent(value = "hi3", aggregateId = "1")

            var partitions: (Unit) -> Map<Int, List<Event>> = { mutableMapOf() }
            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    val sut = EventBus(this, 2)
                    sut.subscribe(stringSubscriber)

                    sut.publish(stringEvent1)
                    sut.publish(stringEvent2)
                    sut.publish(stringEvent3)
                }

            job.cancel()
            awaitAssertion {
                assertThat(stringSubscriber.events).containsExactly(stringEvent1, stringEvent2, stringEvent3)
                assertThat(partitions(Unit)).isEqualTo(
                    mapOf(
                        0 to listOf(stringEvent1, stringEvent2),
                        1 to listOf(stringEvent3),
                    ),
                )
            }
        }

    inner class StringSubscriber : EventSubscriber<StringTestEvent> {
        val events = mutableListOf<StringTestEvent>()

        override fun handle(event: StringTestEvent) {
            events.add(event)
        }
    }

    inner class IntSubscriber : EventSubscriber<IntTestEvent> {
        val events = mutableListOf<IntTestEvent>()

        override fun handle(event: IntTestEvent) {
            events.add(event)
        }
    }

    internal data class StringTestEvent(
        override val aggregateId: String = UUID.randomUUID().toString(),
        val value: String,
    ) : Event {
        override val eventId: UUID = UUID.randomUUID()
        override val datetime: LocalDateTime = LocalDateTime.now()
    }

    internal data class IntTestEvent(val value: Number) : Event {
        override val aggregateId = UUID.randomUUID().toString()
        override val eventId: UUID = UUID.randomUUID()
        override val datetime: LocalDateTime = LocalDateTime.now()
    }
}
