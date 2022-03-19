package me.sagiri.minecraft.ero.gui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import me.sagiri.minecraft.ero.EroHttp
import me.sagiri.minecraft.ero.EroScope
import me.sagiri.minecraft.ero.loliapp.LoliApp
import java.awt.Desktop
import java.net.URI


fun eroGui(r18 : Boolean = false) {
    application {
        val mainWindowState = remember {
            MainWindowState().apply {
                title = "不可以涩涩"
            }
        }
        val applicationConfig = remember {
            ApplicationConfig().apply {
                this.r18 = r18
            }
        }

        if(applicationConfig.r18) {
            mainWindowState.title = "可以涩涩"
        } else {
            mainWindowState.title = "不可以涩涩"
        }

        Window(
            title = mainWindowState.title,
            state = mainWindowState.state,
            visible = mainWindowState.visible,
            icon = mainWindowState.icon,
           onCloseRequest = ::exitApplication
        ) {
            App(mainWindowState, applicationConfig)
        }
    }
}

@Composable
fun App(mainWindowState: MainWindowState, applicationConfig: ApplicationConfig) {
    val pixivImageState = remember {
        PixivImageState().apply {
            image = BitmapPainter(loadImageBitmap(javaClass.getResource("/89783501_p0.jpg").openStream()))
        }
    }
    MaterialTheme {
        Column(
            modifier = Modifier.padding(20.dp)
                .background(Color(0x66ccff)),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(applicationConfig.r18, onCheckedChange = { value ->
                    applicationConfig.r18 = value
                })
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                Text("R18", modifier = Modifier.clickable {
                    applicationConfig.r18 = ! applicationConfig.r18
                })
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    var step by remember { mutableStateOf("") }
                    Button(
                        onClick = {
                            EroScope.launch {
                                val loliAppResponse = LoliApp.get(r18 = if(applicationConfig.r18) 1 else 0, num = 1)
                                if(loliAppResponse != null && loliAppResponse.data.isNotEmpty()) {
                                    val imageData = loliAppResponse.data[0]

                                    pixivImageState.imageUrl = imageData.url
                                    pixivImageState.autor = imageData.author
                                    pixivImageState.title = imageData.title
                                    pixivImageState.pid = imageData.pid.toInt()
                                    pixivImageState.uid = imageData.uid.toInt()

                                    val response = EroHttp.client.get<HttpResponse>(imageData.url) {
                                        onDownload { bytesSentTotal, contentLength ->
                                            step = "${imageData.title} ${bytesSentTotal} / ${contentLength}"
                                        }
                                    }
                                    if(response.status == HttpStatusCode.OK) {
                                        pixivImageState.image = BitmapPainter(loadImageBitmap(response.readBytes().inputStream()))
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Sharp.Favorite, "lock")
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("来点色图")
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = step
                    )
                }
            }

            PixivImageInfo(pixivImageState = pixivImageState)
        }
    }
}

@Composable
fun PixivImageInfo(pixivImageState : PixivImageState) {
    Card {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            SelectionContainer {
                Text(
                    buildAnnotatedString {
                        append("title ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("${pixivImageState.title}")
                        }
                    }
                )
            }

                ClickableText(
                    buildAnnotatedString {
                        append("author ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("${pixivImageState.autor}")
                        }
                    },
                    onClick = {
                        openWeb("https://www.pixiv.net/users/${pixivImageState.uid}")
                    }
                )

                ClickableText(
                    buildAnnotatedString {
                        append("pid ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("${pixivImageState.pid}")
                        }
                    },
                    onClick = {
                        openWeb("https://www.pixiv.net/artworks/${pixivImageState.pid}")
                    }
                )

            pixivImageState.image?.let {
                Image(
                    it,
                    contentDescription = "${pixivImageState.title}"
                )
            }
        }
    }
}

fun openWeb(url: String) {
    Desktop.getDesktop().browse(URI.create(url))
}

fun main() {
    eroGui(false)
}

class MainWindowState {
    var title: String by mutableStateOf("")
    var state : WindowState by mutableStateOf(WindowState())
    var visible : Boolean by mutableStateOf(true)
    var icon : Painter? by mutableStateOf(null)
}

class ApplicationConfig {
    var r18 : Boolean by mutableStateOf(false)
}

class PixivImageState {
    var title : String? by mutableStateOf(null)
    var pid : Int? by mutableStateOf(null)
    var autor : String? by mutableStateOf(null)
    var imageUrl : String? by mutableStateOf(null)
    var uid : Int? by mutableStateOf(null)
    var image : Painter? by mutableStateOf(null)
}