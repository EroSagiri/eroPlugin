package me.sagiri.minecraft.ero.loliapp

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.sagiri.minecraft.ero.EroHttp

@Serializable
data class LoliappResponse(
    val code : Int,
    val msg : String,
    val count : Int,
    val data : List<PixivPicture>
)

@Serializable
data class PixivPicture(
    val pid : Long,
    val p : Int,
    val uid :   Long,
    val title : String,
    val author : String,
    val r18 : Boolean,
    val width : Int,
    val height : Int,
    val tags : List<String>,
    val url : String
)

@Serializable
enum class R18 {
    yes
}

enum class Size {
    original,
    regular,
    small,
    thumb,
    mini
}

object LoliApp {
    private val LOLIAPPAPIURL = "https://api.lolicon.app/setu"

    /**
     * @author sagiri
     * @param r18 0为非 R18，1为 R18，2为混合（在库中的分类，不等同于作品本身的 R18 标识）
     * @param num 一次返回的结果数量，范围为1到100；在指定关键字或标签的情况下，结果数量可能会不足指定的数量
     * @param uid 返回指定uid作者的作品，最多20个
     * @param keyword 返回从标题、作者、标签中按指定关键字模糊匹配的结果，大小写不敏感，性能和准度较差且功能单一，建议使用tag代替
     * @param tag 返回匹配指定标签的作品
     * @param size 返回指定图片规格的地址
     * @param proxy 设置图片地址所使用的在线反代服务
     * @param dataAfter 返回在这个时间及以后上传的作品；时间戳，单位为毫秒
     * @param dateBefore 返回在这个时间及以前上传的作品；时间戳，单位为毫秒
     * @param dsc 设置为任意真值以禁用对某些缩写keyword和tag的自动转换
     */
    suspend fun get(r18 : Int = 0, num : Int = 1, uid : Array<Int>? = null, keyword : String? = null, tag : Array<String>? = null, size : Array<Size> = arrayOf(Size.original), proxy : String = "i.pixiv.cat", dataAfter : Int? = null, dateBefore : Int? = null, dsc : Boolean = false): LoliappResponse? {
        val response = EroHttp.client.get<HttpResponse>(LOLIAPPAPIURL) {
            parameter("r18", r18)
            parameter("num", num)
            if (uid != null) {
                parameter("uid", uid.joinToString(separator = "|"))
            }
            parameter("keyword", keyword)
            if (tag != null) {
                parameter("tag", tag.joinToString(separator = "|"))
            }
            parameter("size", size.joinToString(separator = "|"))
            parameter("proxy", proxy)
            parameter("dataAfter", dataAfter)
            parameter("dataBefore", dateBefore)
            parameter("dsc", dsc)
        }

        if(response.status == HttpStatusCode.OK) {
            val loliappResponse = Json.decodeFromString<LoliappResponse>(response.readText())
            return loliappResponse
        } else {
            return null
        }
    }
}