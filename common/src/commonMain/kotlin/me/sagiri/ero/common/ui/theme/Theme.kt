package me.sagiri.ero.common.me.sagiri.ero.common.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography

interface Theme {
    val name: String
    val darkColor: Colors
    val lightColor: Colors
    val typography: Typography
    val shapes: Shapes
}