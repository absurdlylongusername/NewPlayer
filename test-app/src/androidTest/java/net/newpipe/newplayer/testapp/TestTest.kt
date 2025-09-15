package net.newpipe.newplayer.testapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
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

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testTest() {
        //composeTestRule.onNodeWithText("Start 6502 Stream").performClick()
        onView(ViewMatchers.withText("Start 6502 Stream")).perform(ViewActions.click())

    }
}