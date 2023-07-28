package io.opentelemetry.example.kotlinextension

import java.util.concurrent.CountDownLatch

object Application {

    @JvmStatic
    fun main(vararg args: String) {
        val countDownLatch = CountDownLatch(1)

        CoroutineContextExample().run(countDownLatch)

        countDownLatch.await()
    }
}