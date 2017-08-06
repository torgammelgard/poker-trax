package se.torgammelgard.pokertrax.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.torgammelgard.pokertrax.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TabMain_FragmentActivityTest {

    @Rule
    public ActivityTestRule<TabMain_FragmentActivity> mActivityRule =
            new ActivityTestRule<>(TabMain_FragmentActivity.class);

    @Test
    public void testAddSessionButton() {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.location_dialog)).check(matches(isDisplayed()));
    }
}
