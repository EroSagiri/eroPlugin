package me.sagiri.ero.common.me.sagiri.ero.common.ui.feature.main.image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PixivImage : ImageData {
    override var url: String by mutableStateOf("")
}