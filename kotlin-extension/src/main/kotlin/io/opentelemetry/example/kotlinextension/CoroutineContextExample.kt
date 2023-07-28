package io.opentelemetry.example.kotlinextension

import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.extension.kotlin.asContextElement
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

/**
 * This example aims to show how to embed a Span context within a Kotlin coroutine so that any new span created inside the
 * coroutine will automatically get the Span context provided for the coroutine even if said new span is created after
 * changing the coroutine's thread.
 */
class CoroutineContextExample {
    private val inMemoryExporter: InMemorySpanExporter = InMemorySpanExporter.create()
    private val tracer by lazy { createTracer() }

    fun run(countDownLatch: CountDownLatch) {
        val rootSpan = tracer.spanBuilder("Root span").startSpan()

        val rootScope = rootSpan.makeCurrent()
        println("Root span trace ID: ${rootSpan.spanContext.traceId}")

        // Create the coroutine scope to launch it later.
        val scope = CoroutineScope(Dispatchers.IO)

        // Launching a coroutine with the Span context embedded in its [the coroutine's] context. The `asContextElement` is a Kotlin
        // extension function that ensures that the Span context will be preserved across the life of the coroutine
        // regardless of any "thread change" (coroutine context change) that might happen inside the coroutine.
        // We could also look at the line below as if we'd call `span.makeCurrent` but for a Kotlin coroutine. So after
        // launching the coroutine this way (with the embedded Span context) any new span created inside the coroutine
        // must automatically get our root span as its parent.

        // Bonus: If we wanted to launch a coroutine in a separate thread (not IO, which is the one set in the scope) while
        // keeping the Span context too, we could do so like this:
        // scope.launch(Dispatchers.Main + Context.current().asContextElement())
        scope.launch(Context.current().asContextElement()) {
            // Creating spans that must have the rootSpan as their parent since its context is attached to the
            // coroutine context.
            createChildrenSpans()

            rootScope.close()
            rootSpan.end()

            val finishedSpanItems = inMemoryExporter.finishedSpanItems
            println("First child's trace id: ${finishedSpanItems[1].traceId}") // The same as the root span's.
            println("Second child's trace id: ${finishedSpanItems[2].traceId}") // The same as the root span's.

            // Release the main thread.
            countDownLatch.countDown()
        }
    }

    private suspend fun createChildrenSpans() {
        // Creating a span within the same coroutine context that the coroutine was created with.
        val firstChildSpan = tracer.spanBuilder("First child").startSpan()
        firstChildSpan.end()

        // Switching coroutine's thread to show that the Span context is preserved even after changing the context of
        // the running coroutine.
        withContext(Dispatchers.Default) {
            val secondChild = tracer.spanBuilder("Second child").startSpan()
            secondChild.end()
        }
    }

    private fun createTracer(): Tracer {
        return SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(inMemoryExporter))
                .build()
                .get("TestInstrumentation")
    }
}