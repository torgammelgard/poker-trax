package se.torgammelgard.pokertrax.activities


import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView

import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.ViewMatchers.*

import org.hamcrest.Matchers.*
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is
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