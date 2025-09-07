package net.newpipe.newplayer.testapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@SmallTest
class TestTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testTest() {
        composeTestRule.onNodeWithText("Start 6502 Stream").performClick()
        /*
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            assertEquals(5, 5)
            true
        }
         */
    }
}