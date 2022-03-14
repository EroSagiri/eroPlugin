package me.sagiri.minecraft.ero

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object EroHttp {
     val client = HttpClient(Java) {
        engine {
            pipelining = true
        }

         install(JsonFeature) {
             serializer = KotlinxSerializer()
         }

         install(Logging) {
             logger = Logger.DEFAULT
             level = LogLevel.NONE
         }
    }
}

object EroScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}