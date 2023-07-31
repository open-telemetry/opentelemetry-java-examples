package io.opentelemetry.example.kotlinextension

import kotlinx.coroutines.runBlocking

object Application {

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            CoroutineContextExample().run()
        }
    }
}