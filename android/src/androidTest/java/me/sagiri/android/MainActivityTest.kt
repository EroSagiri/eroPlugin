package me.sagiri.android

import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_STRING = "This is a string"
const val TEST_LONG = 12345678L

// @RunWith is required only if you use a mix of JUnit3 and JUnit4.
@RunWith(AndroidJUnit4::class)
@SmallTest
class LogHistoryAndroidUnitTest {
    @Test
    fun test() {
        assert(true)
    }
}

