package me.sagiri.ero.common.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

actual fun loadImageBitmap(inputStream: InputStream): ImageBitmap {
    return BitmapFactory.decodeStream(inputStream).asImageBitmap()
}