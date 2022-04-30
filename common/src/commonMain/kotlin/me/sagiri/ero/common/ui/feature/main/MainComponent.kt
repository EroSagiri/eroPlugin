package me.sagiri.ero.common.ui.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.main.image.ImageData
import me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.main.image.PixivImage
import me.sagiri.ero.common.ui.navigation.Component
import me.sagiri.ero.common.ui.navigation.RootComponent
import java.util.logging.Logger

class MainComponent(val rootComponent: RootComponent) : Component {
    companion object {
        val images = mutableStateListOf<ImageData>().apply {
            add(PixivImage().apply { url = "QWQ" })
            add(PixivImage().apply { url = "qvc" })
        }

        val logger = Logger.getLogger(javaClass.name)
    }

    @Composable
    override fun render() {
        LaunchedEffect(Unit) {

        }
        Scaffold(

        ) {
            BoxWithConstraints {
                val boxWith = this
                LazyColumn(
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
        Text("${imageData.url}")
    }
}