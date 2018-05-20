package se.torgammelgard.pokertrax.activities

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.R

@RunWith(AndroidJUnit4::class)
@LargeTest
class TabMainFragmentActivityTest {

    @Rule @JvmField
    var mActivityRule = IntentsTestRule<TabMainFragmentActivity>(TabMainFragmentActivity::class.java)

    @Test
    fun testAddSessionButton() {
        onView(withId(R.id.button_add)).perform(click())
        onView(withId(R.id.location_dialog)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenAddSessionScreen() {
        onView(withId(R.id.button_add)).perform(click())
        intended(IntentMatchers.hasComponent(AddSessionActivity::class.java.name))
    }
}
