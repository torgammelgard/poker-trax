package se.torgammelgard.pokertrax.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
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
