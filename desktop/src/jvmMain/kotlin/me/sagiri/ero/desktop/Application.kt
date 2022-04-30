package me.sagiri.ero.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.sagiri.ero.common.QWQ

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            application {
                Window(
                    onCloseRequest = ::exitApplication
                ) {
                    MaterialTheme {
                        QWQ()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewApp() {
    QWQ()
}