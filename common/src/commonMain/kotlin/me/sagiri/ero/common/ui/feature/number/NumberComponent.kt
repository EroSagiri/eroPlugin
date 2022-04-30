package me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.number

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.sagiri.ero.common.ui.navigation.Component
import me.sagiri.ero.common.ui.navigation.RootComponent

class NumberComponent(private val rootComponent: RootComponent, val config: RootComponent.Config.Number) : Component {
    @Composable
    override fun render() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Number")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                rootComponent.back()
                            }
                        ) {
                            Icon(Icons.Filled.NavigateBefore, contentDescription = null)
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    config.n.toString(),
                    fontFamily = MaterialTheme.typography.h1.fontFamily,
                    fontSize = MaterialTheme.typography.h1.fontSize
                )
            }
        }
    }
}