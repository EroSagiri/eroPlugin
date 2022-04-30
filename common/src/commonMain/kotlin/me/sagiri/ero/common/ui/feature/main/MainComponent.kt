package me.sagiri.ero.common.ui.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.push
import me.sagiri.ero.common.ui.navigation.Component
import me.sagiri.ero.common.ui.navigation.RootComponent

class MainComponent(val rootComponent: RootComponent) : Component {
    @Composable
    override fun render() {
        Column {
            val num = remember { mutableStateListOf<Int>().apply { addAll((1..100).map { it }) } }

            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                itemsIndexed(num) { index: Int, item: Int ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).clickable {
                            rootComponent.router.push(RootComponent.Config.Number(item))
                        },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("$item", fontSize = MaterialTheme.typography.h2.fontSize)
                    }
                }
            }
        }
    }
}