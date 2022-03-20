package me.sagiri.minecraft.ero

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import me.sagiri.minecraft.ero.gui.eroGui
import me.sagiri.minecraft.ero.loliapp.LoliApp
import me.sagiri.minecraft.ero.loliapp.Size
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.lang.Runnable
import java.util.regex.Pattern
import kotlin.system.exitProcess

@CommandLine.Command(name = "ero", mixinStandardHelpOptions = true, version = ["1.0.0"], description = ["从LOLIAPI下载图片"])
class Main : Runnable {
    @CommandLine.Option(names = ["r18", "R18", "-R18", "-r18", "-r"], description = ["不可以涩涩"])
    var r18 = false
    @CommandLine.Option(names = ["num", "-num", "-n"], description = ["获取图片的数量"])
    var num = 4
    @CommandLine.Option(names = ["uid", "-uid", "-u"], description = ["返回指定uid作者的作品，最多20个"])
    var uid : Array<Int>? = null
    @CommandLine.Option(names = ["keyword", "-keyword", "-k"], description = ["返回从标题、作者、标签中按指定关键字模糊匹配的结果，大小写不敏感，性能和准度较差且功能单一，建议使用tag代替"])
    var keyword : String? = null
    @CommandLine.Option(names = ["tag", "-tag", "-t"], description = ["返回匹配指定标签的作品"])
    var tags : Array<String>? = null
    @CommandLine.Option(names = ["size", "-szie", "-s"], description = ["返回匹配指定标签的作品", "original", "regular", "small", "thumb", "mini"])
    var size : Array<Size> = arrayOf(Size.original)
    @CommandLine.Option(names = ["proxy", "-proxy", "-p"], description = ["设置图片地址所使用的在线反代服务"])
    var proxy : String = "i.pixiv.re"
    @CommandLine.Option(names = ["dataAfter", "-dataAfter"], description = ["返回在这个时间及以后上传的作品；时间戳，单位为毫秒"])
    var dataAfter : Int? = null
    @CommandLine.Option(names = ["dateBefore", "-dateBefore"], description = ["返回在这个时间及以前上传的作品；时间戳，单位为毫秒"])
    var dateBefore : Int? = null
    @CommandLine.Option(names = ["dsc", "-dsc", "-d"], description = ["设置为任意真值以禁用对某些缩写keyword和tag的自动转换"])
    var dsc : Boolean = false
    @CommandLine.Option(names = ["-g", "-gui", "--gui"])
    var isGui : Boolean = false

    companion object {
        val logger: Logger = LoggerFactory.getLogger("Ero")
    }

    override fun run() {
        runBlocking {
            if(isGui) {
                eroGui(r18 = r18)
            } else {
                val loliappResponse = LoliApp.get(
                    r18 = if (r18) 1 else 0,
                    num = num,
                    uid = uid,
                    keyword = keyword,
                    tag = tags,
                    size = size,
                    proxy = proxy,
                    dataAfter = dataAfter,
                    dateBefore = dateBefore,
                    dsc = dsc
                )
                if (loliappResponse != null) {
                    val downloadJub = mutableListOf<Job>()

                    loliappResponse.data.forEach { image ->
                        val job = EroScope.launch {
                            var imageFormat = "png"
                            val matcher = Pattern.compile("\\.(\\w+?)$").matcher(image.url)
                            if (matcher.find()) {
                                imageFormat = matcher.group(1)
                            }
                            val httpRequest = EroHttp.client.get<HttpResponse>(image.url)
                            if (httpRequest.status == HttpStatusCode.OK) {
                                val imageFile = File("${image.pid}.${imageFormat}")
                                imageFile.writeBytes(httpRequest.readBytes())
                                logger.info("Downloaded ${image.pid} ${image.title}")
                            } else {
                                logger.error("download pid ${image.pid} ${image.title} Failed Code ${httpRequest.status.value}")
                            }
                        }
                        downloadJub.add(job)
                    }

                    downloadJub.map { job ->
                        job.join()
                    }
                } else {
                    logger.error("loliapp API 返回的是空的")
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    if(args.size > 1) {
        val main = Main()

        val status = CommandLine(main).execute(*args)
        exitProcess(status)
    } else {
        eroGui()
    }
}