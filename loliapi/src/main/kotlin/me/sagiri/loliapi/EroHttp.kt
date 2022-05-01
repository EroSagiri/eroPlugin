package me.sagiri.loliapi

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object EroHttp {
     val client = HttpClient(OkHttp) {
         engine {
             pipelining = true
         }

         install(ContentNegotiation) {
             json(
                 Json {
                     prettyPrint = true
                     isLenient = true
                 }
             )
         }

         install(Logging) {
             logger = Logger.DEFAULT
             level = LogLevel.ALL
         }

         install(HttpTimeout) {
//             connectTimeoutMillis = 10000
//             requestTimeoutMillis = 10000
//             socketTimeoutMillis = 10000
         }
     }
}

object EroScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}