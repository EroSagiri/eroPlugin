package me.sagiri.ero.common.me.sagiri.ero.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import me.sagiri.ero.common.me.sagiri.ero.common.ui.theme.pink.PinkTheme


@Composable
fun EroTheme(darkTheme: Boolean = isSystemInDarkTheme(), theme: Theme = PinkTheme, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        theme.darkColor
    } else {
        theme.lightColor
    }

    MaterialTheme(
        colors = colors,
        typography = theme.typography,
        shapes = theme.shapes,
        content = content
    )
}