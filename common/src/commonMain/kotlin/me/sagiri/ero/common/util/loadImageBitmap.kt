package me.sagiri.ero.common.util

import androidx.compose.ui.graphics.ImageBitmap
import java.io.InputStream

expect fun loadImageBitmap(inputStream: InputStream): ImageBitmap