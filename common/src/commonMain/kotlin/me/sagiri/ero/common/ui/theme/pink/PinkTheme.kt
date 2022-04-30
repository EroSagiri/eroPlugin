package me.sagiri.ero.common.me.sagiri.ero.common.ui.theme.pink

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.sagiri.ero.common.me.sagiri.ero.common.ui.theme.Theme

/**
 * ff06b8
 */
object PinkTheme : Theme {
    val pink200 = Color(0xfff48fb1)
    val pink500 = Color(0xffe91e63)
    val pink700 = Color(0xffc2185b)
    val green200 = Color(0xffa1ffa2)
    override val name: String
        get() = "Pink"
    override val darkColor: Colors
        get() = darkColors(
            primary = pink200,
            primaryVariant = pink700,
            secondary = green200
        )
    override val lightColor: Colors
        get() = lightColors(
            primary = pink500,
            primaryVariant = pink700,
            secondary = green200
        )
    override val typography: Typography
        get() = Typography(

        )
    override val shapes: Shapes
        get() = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp)
        )
}