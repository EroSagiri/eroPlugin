package me.sagiri.loliapi

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.random.Random

class LoliApiTest {
    @Test
    fun get() {
        runBlocking {
            val req = LoliApi.get()
            if (req != null) {
                assert(true)
            } else {
                assert(false)
            }
        }
    }

    @Test
    fun getCount() {
        val count = Random.nextInt(4, 10)
        runBlocking {
            val req = LoliApi.get(num = count)
            if (req != null && req.data.size == count) {
                assert(true)
            } else {
                assert(false)
            }
        }
    }
}