package se.torgammelgard.pokertrax.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.R

/**
 * Tests the {@link MainActivity}
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    /**
     * Verify that there is a floating action button for adding a new session
     */
    @Test
    fun testFloatingActionButton_Exists() {
        onView(withId(R.id.fab_add_session)).check(matches(isDisplayed()))
    }

    /**
     * When the floating action button is pressed, an activity @see AddSessionActivity which makes it
     * possible for the user to add a new poker session should appear.
     */
    @Test
    fun testFloatingActionButton_Works() {
        onView(withId(R.id.fab_add_session)).perform(ViewActions.click())
        Intents.intended(hasComponent(AddSessionActivity::class.java.name))
    }
}