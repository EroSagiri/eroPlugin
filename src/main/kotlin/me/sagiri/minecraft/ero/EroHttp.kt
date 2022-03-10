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

suspend fun main(args: Array<String>) {
    /*val response = EroHttp.client.get<HttpResponse>("https://api.lolicon.app/setu/?proxy=i.pixiv.re") {
        parameter("num", 4)
    }
    if(response.status == HttpStatusCode.OK) {
        val imageData : LoliappResponse = Json.decodeFromString(LoliappResponse.serializer(), response.readText())
        val jobs = mutableListOf<Job>()

        imageData.data.forEach { image ->
            val job = EroScope.launch {
                val imageDataResponse = EroHttp.client.get<HttpResponse>(image.url) {
                    onDownload { bytesSentTotal, contentLength ->

                    }
                }

                if(imageDataResponse.status == HttpStatusCode.OK) {
                    val file = File("${image.pid}.png")
                    file.writeBytes(imageDataResponse.readBytes())
                }

            }
            jobs.add(job)
        }
        jobs.map {
            it.join()
        }

    }*/
}

object EroScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

}