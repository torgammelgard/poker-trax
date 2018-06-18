package se.torgammelgard.pokertrax.activities


import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import se.torgammelgard.pokertrax.R

/**
 * Created by torgammelgard on 2017-08-07.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AddSessionActivityTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule<AddSessionActivity>(AddSessionActivity::class.java)

    @Test
    fun testShouldAddNewPlace() {
        onView(withText("Cancel")).perform(click())
        onData(allOf(Is.`is`(instanceOf(String::class.java)), `is`("---new---")))
                .inAdapterView(withId(R.id.location_spinner))
                .perform(click())
        onView(withId(R.id.location_dialog)).check(matches(isDisplayed()))
    }
}