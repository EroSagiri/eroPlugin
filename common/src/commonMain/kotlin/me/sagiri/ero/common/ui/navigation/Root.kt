package me.sagiri.ero.common.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

interface Root {

    val routerState: Value<RouterState<*, Component>>

    sealed class Child {
        object Main : Child();
    }
}

interface Component {
    @Composable
    fun render()
}