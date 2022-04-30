package me.sagiri.ero.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import me.sagiri.ero.common.me.sagiri.ero.common.ui.theme.EroTheme
import me.sagiri.ero.common.ui.navigation.RootComponent

@Composable
fun QWQ() {
    EroTheme {
        val root = remember {
            RootComponent(DefaultComponentContext(LifecycleRegistry()))
        }

        root.render()
    }
}