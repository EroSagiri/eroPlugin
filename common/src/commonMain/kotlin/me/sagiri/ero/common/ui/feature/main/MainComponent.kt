package me.sagiri.ero.common.ui.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import me.sagiri.ero.common.ui.navigation.Component
import me.sagiri.ero.common.ui.navigation.RootComponent

class MainComponent(val rootComponent: RootComponent) : Component {
    @Composable
    override fun render() {
        Column {
            val num = remember { mutableStateListOf<Int>().apply { addAll((1..100).map { it }) } }

            LazyColumn {
                itemsIndexed(num) { index: Int, item: Int ->
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable {
                            rootComponent.router.push(
                                RootComponent.Config.Number(index)
                            )
                        }
                    ) {
                        Text("$index")
                    }
                }
            }
        }
    }
}

class NumberComponent(val rootComponent: RootComponent, val config: RootComponent.Config.Number) : Component {
    @Composable
    override fun render() {
        Column {
            Button(
                onClick = {
                    rootComponent.router.pop()
                }
            ) {
                Text("Back")
            }

            Text(config.n.toString())
        }
    }
}