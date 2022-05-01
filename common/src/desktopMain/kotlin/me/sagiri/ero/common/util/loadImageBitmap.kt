package me.sagiri.ero.common.util

import androidx.compose.ui.graphics.ImageBitmap
import java.io.InputStream

actual fun loadImageBitmap(inputStream: InputStream): ImageBitmap {
    return androidx.compose.ui.res.loadImageBitmap(inputStream)
}