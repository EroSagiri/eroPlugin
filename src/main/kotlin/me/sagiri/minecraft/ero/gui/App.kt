package me.sagiri.minecraft.ero.gui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.*
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
import me.sagiri.minecraft.ero.loliapp.LoliApp
import java.awt.Desktop
import java.net.URI


@OptIn(ExperimentalComposeUiApi::class)
fun eroGui(r18: Boolean = false) {
    application {
        val mainWindowState = remember {
            MainWindowState().apply {
                title = "不可以涩涩"
            }
        }
        val applicationConfig = remember {
            ApplicationConfig().apply {
                this.r18 = r18
                tag.add("萝莉")
            }
        }

        if (applicationConfig.r18) {
            mainWindowState.title = "可以涩涩"
        } else {
            mainWindowState.title = "不可以涩涩"
        }
        Window(
            title = mainWindowState.title,
            state = mainWindowState.state,
            visible = mainWindowState.visible,
            icon = mainWindowState.icon,
            onCloseRequest = ::exitApplication,
            onKeyEvent = { keyEvent ->
                if(keyEvent.type == KeyEventType.KeyDown) {
                    if(keyEvent.key == Key.Spacebar) {
                        if (applicationConfig.getImage != null) {
                            applicationConfig.getImage!!.invoke()
                        }
                    }

                    if(keyEvent.key == Key.R && keyEvent.isAltPressed) {
                        applicationConfig.r18 = !applicationConfig.r18
                    }
                }
                true
            }
        ) {
            App(mainWindowState, applicationConfig)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App(mainWindowState: MainWindowState, applicationConfig: ApplicationConfig) {
    val pixivImageState = remember {
        PixivImageState().apply {
            image = BitmapPainter(loadImageBitmap(javaClass.getResource("/89783501_p0.jpg").openStream()))
        }
    }
    MaterialTheme {
        val scaffoldState  = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState  = scaffoldState
        ) {
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
                        applicationConfig.r18 = !applicationConfig.r18
                    })
                    Spacer(modifier = Modifier.width(20.dp))
                    var tag by remember {
                        mutableStateOf("")
                    }
                    val tagSubmit = {
                        if(tag.isEmpty()) {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("标签是空的")
                            }
                        } else {
                            applicationConfig.tag.add(tag)
                        }
                        tag = ""
                    }
                    TextField(tag, onValueChange = { v ->
                        tag = v
                    }, modifier = Modifier.onPreviewKeyEvent {
                        if(it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                            tagSubmit()
                            true
                        } else {
                            false
                        }
                    })
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = tagSubmit
                    ) {
                        Text("添加")
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Tag")
                    Spacer(Modifier.width(10.dp))
                    LazyRow {
                        itemsIndexed(applicationConfig.tag) { index, item ->
                            PixivImageTag(applicationConfig.tag, index, item)
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        var step by remember { mutableStateOf("") }
                        applicationConfig.getImage  = {
                            scope.launch {
                                if (applicationConfig.getEroButtonEnable) {
                                    applicationConfig.getEroButtonEnable = false
                                    val loliAppResponse = LoliApp.get(
                                        r18 = if (applicationConfig.r18) 1 else 0, num = 1,
                                        tag = applicationConfig.tag.toTypedArray()
                                    )
                                    if (loliAppResponse != null && loliAppResponse.data.isNotEmpty()) {
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
                                        if (response.status == HttpStatusCode.OK) {
                                            pixivImageState.image =
                                                BitmapPainter(loadImageBitmap(response.readBytes().inputStream()))
                                        } else {
                                            scaffoldState.snackbarHostState.showSnackbar("获取图片失败啦 ${imageData.url}")
                                        }
                                    } else {
                                        scaffoldState.snackbarHostState.showSnackbar("获取图片信息失败啦")
                                    }

                                    applicationConfig.getEroButtonEnable = true
                                } else {
                                    // scaffoldState.snackbarHostState.showSnackbar("太快了")
                                }
                            }
                        }
                        Button(
                            onClick = { applicationConfig.getImage?.let { it1 -> it1() } },
                            enabled = applicationConfig.getEroButtonEnable
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
                Spacer(Modifier.height(20.dp))
                PixivImageInfo(pixivImageState = pixivImageState)
            }
        }
    }
}

@Composable
fun PixivImageTag(tag: SnapshotStateList<String>, index: Int, item: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.border(BorderStroke(1.dp, Color.Blue)).padding(horizontal = 10.dp)
    ) {
        SelectionContainer {
            Text(
                text = item
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = {
                tag.removeAt(index)
            }
        ) {
            Icon(Icons.Default.Close, "Close")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PixivImageInfo(pixivImageState: PixivImageState) {
    Card {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            AnimatedContent(targetState = pixivImageState.title) { title ->
                SelectionContainer {
                    Text(
                        buildAnnotatedString {
                            append("title ")
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                            ) {
                                append("$title")
                            }
                        }
                    )
                }
            }

            AnimatedContent(targetState = pixivImageState.autor) { autor ->
                ClickableText(
                    buildAnnotatedString {
                        append("author ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("$autor")
                        }
                    },
                    onClick = {
                        openWeb("https://www.pixiv.net/users/${pixivImageState.uid}")
                    }
                )
            }

            AnimatedContent(pixivImageState.pid) { pid ->
                ClickableText(
                    buildAnnotatedString {
                        append("pid ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("$pid}")
                        }
                    },
                    onClick = {
                        openWeb("https://www.pixiv.net/artworks/${pixivImageState.pid}")
                    }
                )
            }

            AnimatedContent(targetState = pixivImageState.image) { image ->
                image?.let {
                    Image(
                        it,
                        contentDescription = "${pixivImageState.title}"
                    )
                }
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
    var state: WindowState by mutableStateOf(WindowState())
    var visible: Boolean by mutableStateOf(true)
    var icon: Painter? by mutableStateOf(null)
}

class ApplicationConfig {
    var getImage: (() -> Unit)? = null
    var r18: Boolean by mutableStateOf(false)
    var tag: SnapshotStateList<String> = mutableStateListOf()
    var getEroButtonEnable : Boolean by mutableStateOf(true)
}

class PixivImageState {
    var title: String? by mutableStateOf(null)
    var pid: Int? by mutableStateOf(null)
    var autor: String? by mutableStateOf(null)
    var imageUrl: String? by mutableStateOf(null)
    var uid: Int? by mutableStateOf(null)
    var image: Painter? by mutableStateOf(null)
}