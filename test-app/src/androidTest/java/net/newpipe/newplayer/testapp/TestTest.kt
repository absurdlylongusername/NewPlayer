package net.newpipe.newplayer.testapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TestTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testTest() {
        //composeTestRule.onNodeWithText("Start 6502 Stream").performClick()
        onView(ViewMatchers.withText("Start 6502 Stream")).perform(ViewActions.click())
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            true
        }
    }
}