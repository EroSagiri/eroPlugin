package me.sagiri.minecraft.ero

import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import me.sagiri.minecraft.ero.loliapp.LoliApp
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.lang.Runnable
import java.util.logging.Logger

@CommandLine.Command(name = "ero", mixinStandardHelpOptions = true)
class Main : Runnable {
    @CommandLine.Option(names = arrayOf("r18", "R18", "-R18", "-r18"), description = arrayOf("不可以涩涩"))
    var r18 = false
    @CommandLine.Option(names = arrayOf("num", "-num"), description = arrayOf("获取图片的数量"))
    var num = 4;

    companion object {
        val logger = LoggerFactory.getLogger("Ero")
    }

    override fun run() {

    }
}

suspend fun main(args: Array<String>) {
    val main = Main()
    CommandLine(main).parseArgs(*args)

    EroScope.launch {
        val loliappResponse = LoliApp.get(r18 = if (main.r18) 1 else 0, num = main.num)

        if (loliappResponse != null) {
            val downloadJub = mutableListOf<Job>()

                loliappResponse.data.forEach { image ->
                    val job = EroScope.launch {
                        val httpRequest = EroHttp.client.get<HttpResponse>(image.url) {
                            onDownload { bytesSentTotal, contentLength ->
//                                Main.logger.debug("${image.title} $bytesSentTotal / $contentLength")
                            }
                        }
                        if (httpRequest.status == HttpStatusCode.OK) {
                            val imageFile = File("${image.pid}.png")
                            imageFile.writeBytes(httpRequest.readBytes())
                            Main.logger.info("Downloaded ${image.title} ${image.p}")
                        } else {
                            Main.logger.error("download pid ${image.pid} Failed")
                        }
                    }
                    downloadJub.add(job)
                }

            downloadJub.map { job ->
                job.join()
            }
        } else {
            Main.logger.error("loliapp API 返回的是空的")
        }
    }.join()
}