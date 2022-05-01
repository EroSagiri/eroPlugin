package me.sagiri.ero.common.ui.feature.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.main.image.ImageData
import me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.main.image.PixivImage
import me.sagiri.ero.common.ui.navigation.Component
import me.sagiri.ero.common.ui.navigation.RootComponent
import me.sagiri.ero.common.util.loadImageBitmap
import me.sagiri.loliapi.LoliApi
import me.sagiri.loliapi.R18
import java.io.File
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.logging.Logger

class MainComponent(val rootComponent: RootComponent) : Component {
    companion object {
        val images = mutableStateListOf<ImageData>()

        val logger = Logger.getLogger(javaClass.name)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        val lazyState = rememberLazyListState()
        LaunchedEffect(Unit) {
            while (isActive) {
                val data = LoliApi.get(r18 = R18.no, num = 10, tag = arrayOf("loli"))
                data?.data?.forEach {
                    images.add(
                        PixivImage().apply {
                            url = it.url
                            pid = it.pid
                        }
                    )
                }

                delay(5000)
                break
            }
        }
        Scaffold(
            floatingActionButton = {
                if(lazyState.firstVisibleItemIndex == 0 && lazyState.firstVisibleItemScrollOffset == 0) {

                } else {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                lazyState.animateScrollToItem(0, 0)
                            }
                        }
                    ){
                        Text("${lazyState.firstVisibleItemIndex} / ${lazyState.firstVisibleItemScrollOffset}")
                    }
                }
            }
        ) {
            BoxWithConstraints(
                modifier = Modifier.scrollable(
                    state = rememberScrollableState { delte ->
                        println(delte)

                        delte
                    },
                    orientation = Orientation.Vertical
                )
                    .draggable(
                        rememberDraggableState {

                        },
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            println("stop")
                        }
                    )
            ) {
                val boxWith = this
                var l by remember { mutableStateOf(0f) }

                val s by derivedStateOf { lazyState.firstVisibleItemIndex }
                LazyColumn (
                    state = lazyState,
                    modifier = Modifier.draggable(
                        state = rememberDraggableState { delta ->
                            scope.launch {
                                lazyState.scrollToItem(s, lazyState.firstVisibleItemScrollOffset + l.toInt())
                            }
                            l -= delta
                        },
                        orientation = Orientation.Vertical,
                        onDragStarted = {
                            l = 0f
                        },
                        onDragStopped =  {
                            if(lazyState.firstVisibleItemScrollOffset > 0) {
                                lazyState.animateScrollToItem(if(lazyState.firstVisibleItemScrollOffset.dp >  (boxWith.maxHeight /2)) lazyState.firstVisibleItemIndex + 1 else lazyState.firstVisibleItemIndex, 0)
                            }
                        }
                    )
                ) {
                    itemsIndexed(images) { index, item ->
                        ImagePage(item, modifier = Modifier.size(boxWith.maxWidth, boxWith.maxHeight).clickable {
                            logger.info("QWQ")
                        })
                    }
                }

            }
        }
    }
}

@Composable
fun ImagePage(
    imageData: ImageData,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(imageData)
    }
}

@Composable
fun AsyncImage(imageData: ImageData) {
    val url = imageData.url
    var progress by remember { mutableStateOf(0f) }
    val animateProgress by animateFloatAsState(targetValue = progress, animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec)
    val bitmapPainter by produceState<BitmapPainter?>(null) {
        value = withContext(Dispatchers.IO) {
            /*
            val imageFile = File("/sdcard/Download/${imageData.pid}")
            println(imageFile.path)
            if(imageFile.exists()) {
                BitmapPainter(loadImageBitmap(imageFile.readBytes().inputStream()))
            } else {
                try {
                    val response: HttpResponse = client.get(url) {
                        onDownload { bytesSentTotal, contentLength ->
                            progress = bytesSentTotal.toFloat() / contentLength.toFloat()
                        }
                    }
                    if (response.status == HttpStatusCode.OK) {
                        val data = response.readBytes()
                        File(imageData.pid.toString()).writeBytes(data)
                        BitmapPainter(loadImageBitmap(data.inputStream()))
                    } else {
                        println("下载失败状态 ${response.status}")
                        null
                    }
                } catch (e: HttpRequestTimeoutException) {

                    null
                } catch (e: ConnectTimeoutException) {

                    null
                } catch (e: ConnectException) {

                    null
                } catch (e: UnknownHostException) {

                    null
                } catch (e: Exception) {

                    null
                }
            }
             */

            try {
                val response: HttpResponse = client.get(url) {
                    onDownload { bytesSentTotal, contentLength ->
                        progress = bytesSentTotal.toFloat() / contentLength.toFloat()
                    }
                }
                if (response.status == HttpStatusCode.OK) {
                    val data = response.readBytes()
                    BitmapPainter(loadImageBitmap(data.inputStream()))
                } else {
                    println("下载失败状态 ${response.status}")
                    null
                }
            } catch (e: HttpRequestTimeoutException) {

                null
            } catch (e: ConnectTimeoutException) {

                null
            } catch (e: ConnectException) {

                null
            } catch (e: UnknownHostException) {

                null
            } catch (e: Exception) {

                null
            }
        }
    }

    if(bitmapPainter != null) {
        Image(remember { bitmapPainter!! }, contentDescription = null)
    } else {
        CircularProgressIndicator(progress = animateProgress, modifier = Modifier.size(200.dp))
    }
}

val client = HttpClient(OkHttp)