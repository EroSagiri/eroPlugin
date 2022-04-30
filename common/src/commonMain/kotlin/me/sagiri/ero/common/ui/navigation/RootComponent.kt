package me.sagiri.ero.common.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.sagiri.ero.common.ui.feature.main.MainComponent
import me.sagiri.ero.common.ui.feature.main.NumberComponent

class RootComponent(
    private val componentContext: ComponentContext
) : Component, Root, ComponentContext by componentContext {
    val router = router<Config, Component>(
        initialConfiguration = Config.Main,
        childFactory = ::createScreenComponent,
        handleBackButton = true
    )

    override val routerState: Value<RouterState<*, Component>>
        get() = router.state

    @OptIn(ExperimentalDecomposeApi::class)
    @Composable
    override fun render() {
        Children(
            routerState = router.state,
            animation = childAnimation(fade())
        ) {
            it.instance.render()
        }
    }

    fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            is Config.Main -> MainComponent(this)
            is Config.Number -> NumberComponent(this, config)
        }
    }

    fun back() {
        router.pop()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        class Number(val n: Int) : Config() {

        }
    }
}