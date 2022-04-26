package me.sagiri.loliapi

import kotlinx.serialization.Serializable

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
enum class R18() {
    yes {
        override fun toString(): String {
            return "1"
        }
    },
    no {
        override fun toString(): String {
            return "0"
        }
    }
}

enum class Size {
    original,
    regular,
    small,
    thumb,
    mini
}